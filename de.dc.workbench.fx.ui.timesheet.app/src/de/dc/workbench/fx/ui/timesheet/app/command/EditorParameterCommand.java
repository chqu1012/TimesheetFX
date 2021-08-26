package de.dc.workbench.fx.ui.timesheet.app.command;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.IEditor;
import de.dc.workbench.fx.core.model.EmfParameterizedCommand;
import de.dc.workbench.fx.core.modul.service.IEditorService;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;

public class EditorParameterCommand extends EmfParameterizedCommand{

	@Inject IEditorService editorService;

	@Override
	public void execute(Object object) {
		IEditor<?> activeEditor = editorService.getActiveEditor();
		if (activeEditor instanceof MonacoTextEditor editor) {
			editor.executeJSScript(getParameter());
		}
	}
}
