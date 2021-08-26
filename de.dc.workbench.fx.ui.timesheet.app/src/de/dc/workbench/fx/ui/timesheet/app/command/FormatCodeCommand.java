package de.dc.workbench.fx.ui.timesheet.app.command;

public class FormatCodeCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.formatDocument";
	}

	@Override
	protected String getName() {
		return "formatDocument";
	}

}
