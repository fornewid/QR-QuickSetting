package soup.qr.ui.history

import androidx.databinding.ViewDataBinding
import soup.qr.R
import soup.qr.model.BarcodeHistory
import soup.qr.ui.databinding.DataBindingAdapter
import soup.qr.ui.databinding.DataBindingViewHolder
import soup.qr.utils.setOnDebounceClickListener

class BarcodeHistoryListAdapter(
    private val clickListener: (BarcodeHistory) -> Unit,
    private val longClickListener: (BarcodeHistory) -> Unit
) : DataBindingAdapter<BarcodeHistory>() {

    override fun createViewHolder(binding: ViewDataBinding): DataBindingViewHolder<BarcodeHistory> {
        return super.createViewHolder(binding).apply {
            itemView.setOnDebounceClickListener {
                getItem(adapterPosition)?.run(clickListener)
            }
            itemView.setOnLongClickListener {
                getItem(adapterPosition)?.run(longClickListener)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.history_item_barcode
    }
}
