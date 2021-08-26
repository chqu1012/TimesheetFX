package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorZoomInCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.fontZoomIn";
	}

	@Override
	protected String getName() {
		return "zoomIn";
	}

}
