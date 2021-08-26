package de.dc.workbench.fx.ui.timesheet.app.view;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.command.di.EmfFXPlatform;
import de.dc.workbench.fx.core.modul.Command;
import de.dc.workbench.fx.core.modul.service.ICommandService;
import de.dc.workbench.fx.ui.EmfView;
import de.dc.workbench.fx.ui.service.IEmfService;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TimesCommandsView extends EmfView{

	@Inject ICommandService commandService;
	@Inject IEmfService emfService;
	
	public TimesCommandsView() {
		super("Times Commands");
		
		EmfFXPlatform.inject(this);

		var commands = FXCollections.observableArrayList(commandService.getCommandList());
		var filteredCommands = new FilteredList<>(commands);
		var listView = new ListView<Command>();
		listView.setItems(filteredCommands);
		listView.setOnMouseClicked(e->{
			if (e.getClickCount()==2) {
				var selection = listView.getSelectionModel().getSelectedItem();
				commandService.execute(selection);
			}
		});
		listView.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(Command item, boolean empty) {
				super.updateItem(item, empty);
				if (item==null || empty) {
					setGraphic(null);
				}else {
					HBox hbox = new HBox(5);
					hbox.setAlignment(Pos.CENTER_LEFT);
					hbox.getChildren().add(new Label(item.getName()));
					setGraphic(hbox);
				}
			};
		});
		
		var textSearch = new TextField();
		textSearch.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			if (newValue!=null) {
				filteredCommands.setPredicate(p->{
					if(p.getName()==null) {
						return true;
					}
					return p.getName().toLowerCase().contains(newValue.toLowerCase());
				});
			}
		});
		var labelEntries = new Label();

		var parent = new VBox(5);
		parent.getChildren().add(textSearch);
		parent.getChildren().add(listView);

		var hbox = new HBox(5);
		hbox.getChildren().add(new Label("Entries: "));
		hbox.getChildren().add(labelEntries);
		var buttonReload = new Button("Reload");
		buttonReload.setOnAction(e-> commands.setAll(commandService.getCommandList()));
		hbox.getChildren().add(buttonReload);
		parent.getChildren().add(hbox);
		VBox.setVgrow(listView, Priority.ALWAYS);
		
		setContent(parent);
	}
}
