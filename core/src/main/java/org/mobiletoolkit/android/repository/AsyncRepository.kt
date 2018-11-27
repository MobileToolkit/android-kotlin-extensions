package org.mobiletoolkit.android.repository

import com.google.android.gms.tasks.OnCompleteListener

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface AsyncRepository<Entity : Model, Key> : Repository<Entity, Key> {

    fun exists(key: Key, onCompleteListener: OnCompleteListener<Boolean>)

    fun get(key: Key, onCompleteListener: OnCompleteListener<Entity?>)

    fun create(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)

    fun update(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)

    fun delete(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)
    fun delete(key: Key, onCompleteListener: OnCompleteListener<Boolean>)

    fun get(onCompleteListener: OnCompleteListener<List<Entity>>)
}