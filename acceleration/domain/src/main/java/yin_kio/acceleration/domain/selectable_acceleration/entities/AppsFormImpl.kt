package yin_kio.acceleration.domain.selectable_acceleration.entities

internal class AppsFormImpl : AppsForm {

    private var _apps: List<App> = listOf()
    private val _selectedApps: MutableSet<App> = mutableSetOf()

    private val hasSelected: Boolean
        get() = _selectedApps.isNotEmpty()
    private val isAllSelected: Boolean
        get() = _selectedApps.size == _apps.size
    override val selectionStatus: SelectionStatus
        get() = selectionStatus()

    private fun selectionStatus() = when {
        isAllSelected -> SelectionStatus.AllSelected
        hasSelected -> SelectionStatus.HasSelected
        else -> SelectionStatus.NoSelected
    }

    override var apps: List<App>
        get() = _apps
        set(value) {_apps = value}
    override val selectedApps: Collection<App>
        get() = _selectedApps

    override fun switchSelectAll() {
        if (isAllSelected){
            _selectedApps.clear()
        } else {
            _selectedApps.addAll(apps)
        }
    }

    override fun switchSelectApp(app: App) {
        if (_selectedApps.contains(app)){
            _selectedApps.remove(app)
        } else {
            _selectedApps.add(app)
        }
    }

    override fun isAppSelected(app: App): Boolean {
        return _selectedApps.contains(app)
    }
}