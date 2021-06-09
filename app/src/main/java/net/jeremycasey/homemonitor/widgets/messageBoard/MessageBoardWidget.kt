package net.jeremycasey.homemonitor.widgets.messageBoard

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
  private val _postedItems = MutableLiveData<List<PostedItem>>(null)
  val postedItems: LiveData<List<PostedItem>> = _postedItems

  private val _context = context

  fun onAddItem(postedItem: PostedItem) {
    var list = _postedItems.value!!
    // Prevent duplicates
    list = list.filter { it.code != postedItem.code }
    list = list + postedItem
    _postedItems.value = list
  }

  fun onClearItem(postedItem: PostedItem) {
    _postedItems.value = postedItems.value!!.filter { it.code == postedItem.code }
  }
}

@Composable
fun MessageBoardWidget(viewModel: MessageBoardWidgetViewModel) {
  val currentMessageBoard by viewModel.postedItems.observeAsState()

  MessageBoardWidgetView(currentMessageBoard!!, { viewModel.onClearItem(it) })
}

@Composable
fun MessageBoardWidgetView(postedItems: List<PostedItem>, onClearItem: (postedItem: PostedItem) -> Unit) {
  WidgetCard(scrollable = Scrollable.horizontal) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      postedItems.forEach { item ->
        Box(Modifier.padding(10.dp, 0.dp)) {
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
    PostedItem(
      code = "baby_supplies",
      type = "supplies",
      message = "Baby stock up required",
      image = BitmapFactory.decodeResource(LocalContext.current.getResources(), R.drawable.hello_world),
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
