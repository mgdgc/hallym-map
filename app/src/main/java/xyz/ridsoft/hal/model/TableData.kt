package xyz.ridsoft.hal.model

class TableData(var id: String, var title: String) {
    companion object {
        enum class ItemType {
            NORMAL, DIVIDER
        }

        enum class ItemPosition {
            TOP, MIDDLE, BOTTOM, SINGLE
        }
    }

    var content: String? = null
    var description: String? = null
    var icon: Int? = null
    var iconBackgroundColor: Int? = null
    var itemType: ItemType = ItemType.NORMAL
    var itemPosition: ItemPosition = ItemPosition.MIDDLE
    var accessory: Boolean = false
    var selectable: Boolean = false

    constructor(id: String, title: String, content: String?, description: String?) : this(
        id,
        title
    ) {
        this.content = content
        this.description = description
    }

    constructor(id: String, title: String, content: String?, icon: Int) : this(
        id,
        title,
        content,
        null
    ) {
        this.icon = icon
    }

    constructor(
        id: String,
        title: String,
        content: String?,
        icon: Int,
        iconBackgroundColor: Int
    ) : this(id, title, content, icon) {
        this.iconBackgroundColor = iconBackgroundColor
    }

}