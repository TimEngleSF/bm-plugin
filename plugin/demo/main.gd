extends Node2D

# Set Plugin Related Variables
var _plugin_name = "GetHardware"
var _android_plugin

func _ready():
	# Get plugin
	if Engine.has_singleton(_plugin_name):
		_android_plugin = Engine.get_singleton(_plugin_name)
	else:
		printerr("Couldn't find plugin " + _plugin_name)

func requirePerfomanceMode():
	var performanceMode = false
	if _android_plugin:
		var total_ram = _android_plugin.getTotalRAM()
		var cpu_info = _android_plugin.getCPUInfo()
		var cpu_speed = _android_plugin.getCPUSpeed()
		var storage = _android_plugin.getNonVolatileMemoryInfo()
		var os_version = _android_plugin.getAndroidVersion()
		var os_int = int(os_version.split(".")[1])
		if os_int <= 10 or total_ram <= 4000 or storage <= 50000:
			performanceMode = true
	return performanceMode

func _on_Button_pressed():
	if _android_plugin:
		print("PERFORMANCE MODE: ", requirePerfomanceMode())
	else:
		print("Error init")
