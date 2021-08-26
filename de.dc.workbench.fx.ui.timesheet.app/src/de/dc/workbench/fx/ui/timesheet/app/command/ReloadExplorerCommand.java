package de.dc.workbench.fx.ui.timesheet.app.command;

import de.dc.workbench.fx.core.command.di.EmfFXPlatform;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.core.model.EmfCommand;
import de.dc.workbench.fx.ui.views.ProjectExplorer;

public class ReloadExplorerCommand implements EmfCommand {

	@Override
	public void execute(Object input) {
		IEventBroker eventBroker = EmfFXPlatform.getInstance(IEventBroker.class);
		eventBroker.post(ProjectExplorer.EVENT_TOPIC_RELOAD);
	}

}
