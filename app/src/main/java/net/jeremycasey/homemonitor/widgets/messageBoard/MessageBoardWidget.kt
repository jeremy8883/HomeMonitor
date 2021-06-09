package net.jeremycasey.homemonitor.widgets.messageBoard

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.R
import net.jeremycasey.homemonitor.composables.Scrollable
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.private.messageBoardItems
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme

class MessageBoardWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MessageBoardWidgetViewModel(_context) as T
  }
}

class MessageBoardWidgetViewModel(context: Context) : ViewModel() {
  private val _postedItems = MutableLiveData<List<MessageItem>>(listOf())
  val postedItems: LiveData<List<MessageItem>> = _postedItems

  private val _context = context

  fun onIntentReceived(intent: Intent) {
    val action = intent.action
    if (action == null || action != "net.jeremycasey.homemonitor.POST_MESSAGE") return

    val code = intent.extras?.getString("code")
    if (code == null) return

    val messageItemConfig = messageBoardItems.get(code)
    if (messageItemConfig == null) return

    val messageItem = MessageItem(
      code = code,
      type = messageItemConfig.type,
      message = messageItemConfig.message,
      image = BitmapFactory.decodeResource(_context.getResources(), messageItemConfig.imageResource),
    )

    onAddItem(messageItem)
  }

  fun onAddItem(postedItem: MessageItem) {
    var list = _postedItems.value!!
    // Prevent duplicates
    list = list.filter { it.code != postedItem.code }
    list = list + postedItem
    _postedItems.value = list
  }

  fun onClearItem(postedItem: MessageItem) {
    _postedItems.value = postedItems.value!!.filter { it.code != postedItem.code }
  }
}

@Composable
fun MessageBoardWidget(viewModel: MessageBoardWidgetViewModel) {
  val postedItems by viewModel.postedItems.observeAsState()

  MessageBoardWidgetView(postedItems!!, { viewModel.onClearItem(it) })
}

@Composable
fun MessageBoardWidgetView(postedItems: List<MessageItem>, onClearItem: (postedItem: MessageItem) -> Unit) {
  WidgetCard(scrollable = Scrollable.horizontal) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      postedItems.forEach { item ->
        Box(Modifier.padding(10.dp, 0.dp).clickable { onClearItem(item) }) {
          Image(
            painter = BitmapPainter(item.image.asImageBitmap()),
            contentDescription = item.message,
            modifier = Modifier.width(150.dp).height(150.dp)
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  val mockMessageBoard = listOf(
    MessageItem(
      code = "baby_supplies",
      type = "supplies",
      message = "Baby stock up required",
      image = BitmapFactory.decodeResource(LocalContext.current.getResources(), R.drawable.message_board_baby_supplies),
    ),
    MessageItem(
      code = "toilet_paper",
      type = "supplies",
      message = "Toilet paper is required",
      image = BitmapFactory.decodeResource(LocalContext.current.getResources(), R.drawable.message_board_toilet_paper),
    )
  )

  HomeMonitorTheme {
    MessageBoardWidgetView(mockMessageBoard, { })
  }
}

@Preview(showBackground = true)
@Composable
fun NoItemsPreview() {
  HomeMonitorTheme {
    MessageBoardWidgetView(listOf(), { })
  }
}
