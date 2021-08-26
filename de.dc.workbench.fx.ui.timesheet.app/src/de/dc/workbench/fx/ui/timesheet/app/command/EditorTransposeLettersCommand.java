package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorTransposeLettersCommand extends BaseEditorTriggerCommand{

	@Override
	protected String getAction() {
		return "editor.action.transposeLetters";
	}

	@Override
	protected String getName() {
		return "transposeLetters";
	}

}
