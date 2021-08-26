package de.dc.workbench.fx.ui.timesheet.app.command;

public class EditorChangeFontSizeCommand extends BaseJavaScriptCommand{

	private int fontSize = 12;
	
	@Override
	protected String javascript() {
		fontSize = fontSize+1;
		return "editorView.updateOptions({ fontSize: "+fontSize+" });";
	}
}
