package org.mobiletoolkit.android.firebase.firestore

import android.support.v7.widget.RecyclerView

/**
 * Created by Sebastian Owodzin on 16/12/2018.
 */
abstract class FirestoreRecyclerViewAdapter<ViewHolder : RecyclerView.ViewHolder, Entity : FirestoreModel>(
    private val repository: SimpleFirestoreRepository<Entity>
) : RecyclerView.Adapter<ViewHolder>() {

    private val repositoryListener: FirestoreRepositoryListener<List<Entity>, Entity> = { entities, _, _ ->
        data = entities ?: listOf()

        notifyDataSetChanged()
    }

    protected open var data: List<Entity> = listOf()

    init {
        reloadRepositoryData()
    }

    override fun getItemCount(): Int = data.count()

    protected open fun getDataItem(position: Int): Entity? = data[position]

    protected fun reloadRepositoryData() {
        repository.get(repositoryListener)
    }
}