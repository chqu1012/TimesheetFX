package de.dc.workbench.fx.ui.timesheet.app.command;

import de.dc.workbench.fx.core.model.EmfCommand;
import de.dc.workbench.fx.core.workspace.FileResource;

public class ExportToTimesheetCommand implements EmfCommand{

//	private TimesheetFile timesheetFile = new TimesheetFile();
	
	@Override
	public void execute(Object input) {
		if (input instanceof FileResource) {
//			FileResource resource = (FileResource) input;
//			File file = resource.getFile();
//			
//			IXtextService xtextService = EmfFXPlatform.getInstance(IXtextService.class);
//			IEventBroker eventBroker = EmfFXPlatform.getInstance(IEventBroker.class);
//			
//			Resource res = xtextService.getResourceBy(TimesheetApplication.ID, file.getAbsolutePath());
//	        EObject root = res.getContents().get(0);
//	        if (root instanceof Timesheet) {
//	        	Timesheet timesheet = (Timesheet) root;
//	        	String parent = file.getParent();
//	        	timesheetFile.write(timesheet, parent+"/"+file.getName().replace(".spelltimesheet", ".timesheet"));
//	        	
//	        	eventBroker.post(ProjectExplorer.EVENT_TOPIC_RELOAD);
//	        }
		}
	}

}
