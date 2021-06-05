package ipvc.estg.epic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ipvc.estg.epic.R
import ipvc.estg.epic.api.feed

class PostAdapter (val postModel: MutableList<feed>): RecyclerView.Adapter<PostViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_feed, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.bindView(postModel[position])
    }

    override fun getItemCount(): Int {
        return postModel.size
    }

}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    //private val fotoUtilizadorAmigo: TextView = itemView.findViewById(R.id.fotoUtilizadorAmigo)
    private val DataHoraAtividade: TextView = itemView.findViewById(R.id.DataHoraAtividade)
    private val DescricaoAtividade: TextView = itemView.findViewById(R.id.DescricaoAtividade)
    private val DadosAtividadeDistancia: TextView = itemView.findViewById(R.id.DadosAtividadeDistancia)
    private val DadosAtividadePassos: TextView = itemView.findViewById(R.id.DadosAtividadePassos)
    private val DadosAtividadeTempo: TextView = itemView.findViewById(R.id.DadosAtividadeTempo)
    private val imagemAtividadeMapa: ImageView = itemView.findViewById(R.id.imagemAtividadeMapa)

    fun bindView(postModel: feed){
        //fotoUtilizadorAmigo.text = postModel.id.toString()
        DataHoraAtividade.text = postModel.data_fim
        DescricaoAtividade.text = postModel.utilizador_id.toString()
        DadosAtividadeDistancia.text = "Distância: " + postModel.distancia.toString()
        DadosAtividadePassos.text = "Passos: " +postModel.passos.toString()
        DadosAtividadeTempo.text = "Tempo: " +postModel.tempo.toString()
        //Picasso.get().load(postModel.imagem_mapa).into(imagemAtividadeMapa)

    }

}