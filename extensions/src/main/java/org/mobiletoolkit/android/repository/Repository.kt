package org.mobiletoolkit.android.repository

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface Repository<Entity : Model<Identifier>, Identifier> {

    fun exists(identifier: Identifier): Boolean

    fun get(identifier: Identifier): Entity?

    fun create(entity: Entity): Boolean

    fun update(entity: Entity): Boolean

    fun delete(entity: Entity): Boolean
    fun delete(identifier: Identifier): Boolean

    fun get(): List<Entity>
}