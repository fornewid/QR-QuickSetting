package soup.qr.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import soup.qr.databinding.HistoryItemBinding
import soup.qr.model.BarcodeHistory
import soup.qr.ui.history.BarcodeHistoryListAdapter.BarcodeHistoryViewHolder
import soup.qr.ui.setBarcodeFormatText
import soup.qr.ui.setBarcodeImage
import soup.qr.ui.setOnDebounceClickListener

class BarcodeHistoryListAdapter(
    private val clickListener: (BarcodeHistory) -> Unit,
    private val longClickListener: (BarcodeHistory) -> Unit
) : RecyclerView.Adapter<BarcodeHistoryViewHolder>() {

    private val items = mutableListOf<BarcodeHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(layoutInflater, parent, false)
        return BarcodeHistoryViewHolder(binding).apply {
            itemView.setOnDebounceClickListener {
                getItem(adapterPosition)?.run(clickListener)
            }
            itemView.setOnLongClickListener {
                getItem(adapterPosition)?.run(longClickListener)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: BarcodeHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(list: List<BarcodeHistory>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): BarcodeHistory? {
        return items.getOrNull(position)
    }

    class BarcodeHistoryViewHolder(
        private val binding: HistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BarcodeHistory?) {
            if (item != null) {
                binding.barcodeImage.setBarcodeImage(item.format, item.rawValue)
                binding.barcodeTypeLabel.setBarcodeFormatText(item.format)
                binding.barcodeDisplayText.text = item.rawValue
            }
        }
    }
}
