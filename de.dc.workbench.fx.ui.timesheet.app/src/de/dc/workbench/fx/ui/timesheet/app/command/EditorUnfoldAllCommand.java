package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorUnfoldAllCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.unfoldAll";
	}

	@Override
	protected String getName() {
		return "unfold";
	}

}
