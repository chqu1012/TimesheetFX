package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorToTitleCaseCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.transformToTitlecase";
	}

	@Override
	protected String getName() {
		return "toTitleCase";
	}

}
