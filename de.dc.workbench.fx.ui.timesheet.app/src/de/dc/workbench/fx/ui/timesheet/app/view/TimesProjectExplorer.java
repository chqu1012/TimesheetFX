package de.dc.workbench.fx.ui.timesheet.app.view;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.modul.service.ICommandService;
import de.dc.workbench.fx.core.service.ISelectionService;
import de.dc.workbench.fx.core.workspace.FileResource;
import de.dc.workbench.fx.ui.views.ProjectExplorer;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class TimesProjectExplorer extends ProjectExplorer{

	@Inject ISelectionService selectionService;
	@Inject ICommandService commandService;
	
	private static final String COMMAND_RELOAD = "de.dc.emf.fx.workbench.jmetro.ui.times.app.command.ReloadExplorerCommand";
	private static final String COMMAND_EXPORT_TIMESHEET = "de.dc.emf.fx.workbench.jmetro.ui.times.app.command.ExportToTimesheetCommand";
	
	@Override
	protected void onTreeViewMouseClicked(MouseEvent event) {
		super.onTreeViewMouseClicked(event);
		executeMenu.getItems().clear();
		var selection = selectionService.getTreeSelection();

		createMenu("Reload Explorer", ()->commandService.execute(COMMAND_RELOAD, null));
		
		if (selection instanceof FileResource resource && resource.getFile().getName().endsWith("spelltimesheet")) {
			createMenu("Export To Timesheet", ()->commandService.execute(COMMAND_EXPORT_TIMESHEET, null));
		}
	}
	
	public void createMenu(String text, Runnable runnable) {
		MenuItem menuReload = new MenuItem(text);
		menuReload.setOnAction(e-> runnable.run());
		executeMenu.getItems().add(menuReload);
	}
}
