package yin_kio.acceleration.domain.selectable_acceleration.entities

internal class AppsFormImpl : AppsForm {

    private var _apps: List<String> = listOf()
    private val _selectedApps: MutableSet<String> = mutableSetOf()

    override val hasSelected: Boolean
        get() = _selectedApps.isNotEmpty()
    override val isAllSelected: Boolean
        get() = _selectedApps.size == _apps.size
    override val selectionStatus: SelectionStatus
        get() = selectionStatus()

    private fun selectionStatus() = when {
        isAllSelected -> SelectionStatus.AllSelected
        hasSelected -> SelectionStatus.HasSelected
        else -> SelectionStatus.NoSelected
    }

    override var apps: List<String>
        get() = _apps
        set(value) {_apps = value}
    override val selectedApps: Collection<String>
        get() = _selectedApps

    override fun switchSelectAll() {
        if (isAllSelected){
            _selectedApps.clear()
        } else {
            _selectedApps.addAll(apps)
        }
    }

    override fun switchSelectApp(packageName: String) {
        _selectedApps.add(packageName)
    }

    override fun isAppSelected(packageName: String): Boolean {
        return _selectedApps.contains(packageName)
    }
}