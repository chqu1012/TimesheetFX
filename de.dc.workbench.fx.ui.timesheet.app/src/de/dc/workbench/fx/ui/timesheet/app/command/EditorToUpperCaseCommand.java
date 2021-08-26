package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorToUpperCaseCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.transformToUppercase";
	}

	@Override
	protected String getName() {
		return "toUpperCase";
	}

}
