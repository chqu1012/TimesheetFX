package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorFoldAllCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.foldAll";
	}

	@Override
	protected String getName() {
		return "fold";
	}

}
