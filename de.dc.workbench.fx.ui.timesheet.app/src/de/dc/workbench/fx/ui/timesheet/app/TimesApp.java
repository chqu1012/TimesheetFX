package de.dc.workbench.fx.ui.timesheet.app;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.command.di.EmfFXPlatform;
import de.dc.workbench.fx.core.workspace.di.WorkspaceModule;
import de.dc.workbench.fx.core.xtext.IXtextService;
import de.dc.workbench.fx.core.xtext.di.XtextModule;
import de.dc.workbench.fx.ui.EmfApplication;
import de.dc.workbench.fx.ui.EmfWorkbench;
import de.dc.workbench.fx.ui.monaco.di.MonacoModule;
import de.dc.workbench.fx.ui.monaco.service.IMonacoLanguageService;
import de.dc.workbench.fx.ui.timesheet.spell.TimesheetDslStandaloneSetup;

public class TimesApp extends EmfApplication{

	public static final String ID = "Timesheet";
	
	@Inject IXtextService xtextService;
	
	@Override
	protected void addModules() {
		addModule(new WorkspaceModule());
		addModule(new XtextModule());
		addModule(new MonacoModule());
	}
	
	@Override
	protected void initStandaloneWorkbench(EmfWorkbench workbench) {
		super.initStandaloneWorkbench(workbench);
		
		TimesheetDslStandaloneSetup setup = new TimesheetDslStandaloneSetup();
		xtextService.register(ID, setup);
		xtextService.registerByFileExtension("spelltimesheet", setup);
		
		EmfFXPlatform.getInstance(IMonacoLanguageService.class).loadAll();
	}
	
	@Override
	protected boolean isStandaloneApp() {
		return true;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
