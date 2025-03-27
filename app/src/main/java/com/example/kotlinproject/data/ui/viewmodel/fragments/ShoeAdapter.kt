import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.Shoe


class ShoeAdapter(private val shoes: List<Shoe>) : RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shoe, parent, false)
        return ShoeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        holder.bind(shoes[position])
    }

    override fun getItemCount(): Int = shoes.size

    class ShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shoeImage: ImageView = itemView.findViewById(R.id.item_shoe_image)
        private val shoeTitle: TextView = itemView.findViewById(R.id.item_shoe_title)
        private val shoeDesc: TextView = itemView.findViewById(R.id.item_shoe_description)

        fun bind(shoe: Shoe) {
            shoeTitle.text = shoe.title
            shoeDesc.text = shoe.description

            Log.d("ShoeAdapter", "Loading image: ${shoe.imageUrl}")

            Glide.with(itemView.context)
                .load(shoe.imageUrl)
                .into(shoeImage)

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shoe.articleUrl))
                itemView.context.startActivity(intent)
            }
        }
    }
}
