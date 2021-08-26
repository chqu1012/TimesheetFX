package de.dc.workbench.fx.ui.timesheet.app.command;

public abstract class BaseEditorTriggerCommand extends BaseJavaScriptCommand{

	@Override
	protected String javascript() {
		return "editorView.trigger('"+getName()+"', '"+getAction()+"');";
	}

	protected abstract String getAction();

	protected abstract String getName();
}
