package com.example.androidmvvm.view.recyclerViewAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_repositorio.view.*

class AdapterRepositorios(
    private val context: Context,
    var mValues: ArrayList<Repositorio> = arrayListOf(),
    private val interacaoComLista: InteracaoComLista<Repositorio>
) : RecyclerView.Adapter<AdapterRepositorios.ViewHolder>() {

    private val mOnclickListener: View.OnClickListener = View.OnClickListener { view ->
        val repositorio = view.tag as Repositorio

        interacaoComLista.selecionou(repositorio)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repositorio,
                null
            )
        )
    }

    override fun getItemCount() = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repositorio = mValues[position]

        holder.tvNomerepositorio.text = repositorio.nomeRepositorio
        holder.tvDescricaoRepositorio.text = repositorio.descricaoRepositorio
        holder.tvUserName.text = repositorio.proprietario.nomeAutor
        holder.tvNumeroForks.text = repositorio.quantidadeDeForks.toString()
        holder.tvNumeroStars.text = repositorio.quantidadeDeEstrelas.toString()

        ContextCompat.getDrawable(context, R.mipmap.image_placeholder_350x350)?.let {
            Picasso.get()
                .load(repositorio.proprietario.avatar_url)
                .resize(50, 50)
                .centerCrop()
                .placeholder(it)
                .into(holder.imageAvatar)
        }

        with(holder.mView) {
            tag = repositorio
            setOnClickListener(mOnclickListener)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var tvNomerepositorio: TextView = mView.tvNomerepositorio
        var tvDescricaoRepositorio: TextView = mView.tvDescricaoRepositorio
        var tvNumeroForks: TextView = mView.tvNumeroForks
        var tvNumeroStars: TextView = mView.tvNumeroStars
        var tvUserName: TextView = mView.tvUserName
        var imageAvatar: ImageView = mView.imageAvatar
    }
}