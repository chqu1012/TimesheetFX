package de.dc.workbench.fx.ui.timesheet.app.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.EventTopic;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.core.model.EmfOutlineContext;
import de.dc.workbench.fx.core.service.ISelectionService;
import de.dc.workbench.fx.core.xtext.IXtextService;
import de.dc.workbench.fx.ui.EmfTreeView;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;
import de.dc.workbench.fx.ui.timesheet.DayModel;
import de.dc.workbench.fx.ui.timesheet.MonthModel;
import de.dc.workbench.fx.ui.timesheet.TimesFactory;
import de.dc.workbench.fx.ui.timesheet.TimesPackage;
import de.dc.workbench.fx.ui.timesheet.Timesheet;
import de.dc.workbench.fx.ui.timesheet.TrackingData;
import de.dc.workbench.fx.ui.timesheet.WeekModel;
import de.dc.workbench.fx.ui.timesheet.control.StatisticsPane;
import de.dc.workbench.fx.ui.timesheet.provider.DayModelItemProvider;
import de.dc.workbench.fx.ui.timesheet.provider.MonthModelItemProvider;
import de.dc.workbench.fx.ui.timesheet.provider.TimesItemProviderAdapterFactory;
import de.dc.workbench.fx.ui.timesheet.provider.TrackingDataItemProvider;
import de.dc.workbench.fx.ui.timesheet.provider.WeekModelItemProvider;
import de.dc.workbench.fx.ui.timesheet.util.TotalCalculator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

public class TimesOutline extends EmfTreeView<Timesheet> {

	@Inject IEventBroker eventBroker;
	@Inject IXtextService xtextServie;
	@Inject ISelectionService selectionService;

	private TotalCalculator totalCalculator = new TotalCalculator();
	private StatisticsPane statisticsPane = new StatisticsPane();
	
	public TimesOutline() {
		super("Times Outline");

		eventBroker.register(this);
		setShowContextMenu(false);

		var splitPane = new SplitPane();
		splitPane.getItems().add(treeView);
		splitPane.getItems().add(statisticsPane);
		setContent(splitPane);
	}

	@Subscribe
	public void subscribeCurrentSelectedEditor(EventContext<Tab> context) {
		if (context.match(EventTopic.CURRENT_SELECTED_EDITOR) && context.getInput() instanceof MonacoTextEditor editor) {
			var input = editor.getInput();
			var resourceSet = xtextServie.getResourceSetByExtension(input.getFile().getAbsolutePath());
			var resource = resourceSet.createResource(URI.createURI("dummy:/example.spelltimesheet"));
			var in = new ByteArrayInputStream(input.getText().getBytes());
			try {
				resource.load(in, resourceSet.getLoadOptions());
				var eObject = resource.getContents().get(0);
				var timesheet = (Timesheet) eObject;
				setInput(timesheet);
				statisticsPane.setInput(timesheet);
				expand(rootItem);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Subscribe
	public void subscribeMonacoEditorTextChanged(EventContext<EmfOutlineContext> context) throws IOException {
		if (context.match("/monaco/editor/text/changed")) {
			var input = context.getInput();
			var file = input.getFile();
			if (file == null) {
				return;
			}

			var resourceSet = xtextServie.getResourceSetByExtension(file);
			var resource = resourceSet.createResource(URI.createURI("dummy:/example.spelltimesheet"));
			var in = new ByteArrayInputStream(input.getText().getBytes());
			resource.load(in, resourceSet.getLoadOptions());
			var eObject = resource.getContents().get(0);
			setInput((Timesheet) eObject);
			expand(rootItem);
		}
	}

	protected void expand(TreeItem<?> item) {
		if (item != null && !item.isLeaf()) {
			item.setExpanded(true);
			for (TreeItem<?> child : item.getChildren()) {
				if (child.getValue() instanceof DayModel) {
					continue;
				}
				expand(child);
			}
		}
	}

	@Override
	protected EObject createRootModel() {
		return TimesFactory.eINSTANCE.createTimesheet();
	}

	@Override
	protected EPackage getEPackage() {
		return TimesPackage.eINSTANCE;
	}

	@Override
	protected AdapterFactory getModelItemProviderAdapterFactory() {
		return new TimesItemProviderAdapterFactory() {
			@Override
			public Adapter createMonthModelAdapter() {
				if (monthModelItemProvider == null) {
					monthModelItemProvider = new MonthModelItemProvider(this) {
						@Override
						public String getText(Object object) {
							if (object instanceof MonthModel c) {
								var month = c.getMonth();
								if (month==null) {
									return StringUtils.EMPTY;
								}
								return month.toString() + " (total: " + totalCalculator.doSwitch(c) + "h)";
							}
							return super.getText(object);
						}
					};
				}
				return super.createMonthModelAdapter();
			}

			@Override
			public Adapter createWeekModelAdapter() {
				if (weekModelItemProvider == null) {
					weekModelItemProvider = new WeekModelItemProvider(this) {
						@Override
						public String getText(Object object) {
							if (object instanceof WeekModel week) {
								return "Week " + week.getWeekNnumber() + " (total: " + totalCalculator.doSwitch(week)
										+ "h)";
							}
							return super.getText(object);
						}
					};
				}
				return super.createWeekModelAdapter();
			}

			@Override
			public Adapter createDayModelAdapter() {
				if (dayModelItemProvider == null) {
					dayModelItemProvider = new DayModelItemProvider(this) {
						@Override
						public String getText(Object object) {
							if (object instanceof DayModel day) {
								var date = day.getDate();
								if (date == null) {
									return StringUtils.EMPTY;
								}
								return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " (total: "
										+ totalCalculator.doSwitch(day) + "h)";
							}
							return super.getText(object);
						}
					};
				}
				return super.createDayModelAdapter();
			}

			@Override
			public Adapter createTrackingDataAdapter() {
				if (trackingDataItemProvider == null) {
					trackingDataItemProvider = new TrackingDataItemProvider(this) {
						@Override
						public String getText(Object object) {
							if (object instanceof TrackingData data) {
								var start = data.getStart();
								var end = data.getEnd();
								return format(start) + "-" + format(end) + " -> " + data.getProjectKey() + " ("
										+ totalCalculator.doSwitch(data) + "h)";
							}
							return super.getText(object);
						}
					};
				}
				return super.createTrackingDataAdapter();
			}
		};
	}

	protected String format(LocalTime time) {
		if (time == null) {
			return "N/A";
		}
		return time.format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	protected double total(LocalTime time1, LocalTime time2) {
		var diff = ChronoUnit.MINUTES.between(time2, time1);
		return diff / 60.0;
	}
}
