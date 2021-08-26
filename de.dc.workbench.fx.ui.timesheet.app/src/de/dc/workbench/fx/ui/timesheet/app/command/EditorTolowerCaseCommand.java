package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorTolowerCaseCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.transformToLowercase";
	}

	@Override
	protected String getName() {
		return "toLowerCase";
	}

}
