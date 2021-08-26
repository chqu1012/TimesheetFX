package de.dc.workbench.fx.ui.timesheet.app.command;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.IEditor;
import de.dc.workbench.fx.core.model.EmfCommand;
import de.dc.workbench.fx.core.modul.service.IEditorService;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;

public class MonacoCopyLineCommand implements EmfCommand{

	@Inject IEditorService editorService;
	
	@Override
	public void execute(Object input) {
		IEditor<?> activeEditor = editorService.getActiveEditor();
		if (activeEditor instanceof MonacoTextEditor editor) {
			editor.executeJSScript("editorView.trigger('keyboard', 'type', {text: `text on multiple line`});");
		}
	}
}
