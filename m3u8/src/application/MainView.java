package application;

import java.io.File;
import java.util.ArrayList;

import application.component.ProgressContainer;
import application.dto.EXTINF;
import application.utils.CommonUtility;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainView extends Application {
	private Stage primaryStage;
	private TextField urlTextField;
	private TextField dirTextField;
	private Button downloadButton;
	private GridPane gridPane;
	public static int rowIndex = 1;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		this.primaryStage = primaryStage;
		primaryStage.setTitle("下载m3u8视频");
		ObservableList<javafx.scene.image.Image> icons = primaryStage.getIcons();
		icons.add(CommonUtility.getImage("title.png"));

		VBox root = new VBox();
//		root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
		Scene scene = new Scene(root, 500, 300, Color.WHITE);
		primaryStage.setResizable(false);

		GridPane pane = new GridPane();
//		pane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		VBox.setMargin(pane, new Insets(10, 50, 0, 50));
		root.getChildren().add(pane);
		pane.setVgap(5);

		Label urlLabel = new Label("下载链接");
		urlTextField = new TextField();
		urlTextField.setPrefWidth(350);
		Label dirLabel = new Label("保存路径");

		dirTextField = new TextField();
		dirTextField.setPrefWidth(310);
		dirTextField.setTooltip(new Tooltip("双击、或按Enter键选择目录"));
		downloadButton = new Button("下载");
		downloadButton.setPrefWidth(40);
		HBox dirHBox = new HBox(dirTextField, downloadButton);
		initEventHandler();

		GridPane.setHalignment(urlLabel, HPos.RIGHT);
		pane.add(urlLabel, 0, 0);
		GridPane.setHalignment(urlTextField, HPos.LEFT);
		pane.add(urlTextField, 1, 0);

		GridPane.setHalignment(dirLabel, HPos.RIGHT);
		pane.add(dirLabel, 0, 1);
		pane.add(dirHBox, 1, 1);

//		ScrollPane scrollPane = new ScrollPane();
//		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
//		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
//		gridPane = new GridPane();
//		gridPane.setVgap(5);
//		scrollPane.setContent(gridPane);
//		pane.add(scrollPane, 0, 2, 2, 1);

		TableView<EXTINF> tableView = new TableView<EXTINF>();
		TableColumn firstColumn = new TableColumn("");
		firstColumn.setCellFactory((col) -> {
			TableCell<EXTINF, String> cell = new TableCell<EXTINF, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					this.setText(null);
					this.setGraphic(null);

					if (!empty) {
						int rowIndex = this.getIndex() + 1;
						this.setText(String.valueOf(rowIndex));
					}
				}
			};
			return cell;
		});
		TableCell<EXTINF, String> tableCell = new TableCell<EXTINF, String>();
//		firstColumn.setCellFactory(new Callback<TableColumn<EXTINF, String>, TableCell<EXTINF, String>>() {
//
//			@Override
//			public TableCell<EXTINF, String> call(TableColumn<EXTINF, String> param) {
//				return null;
//			}
//		});
		TableColumn secondColumn = new TableColumn("名称");
		TableColumn thirdColumn = new TableColumn("已完成");
		TableColumn fourColumn = new TableColumn("保存路径");
		fourColumn.setCellValueFactory(new PropertyValueFactory<EXTINF, String>("dir"));
//		
		// https://blog.csdn.net/servermanage/article/details/102317726
		// https://blog.csdn.net/MrChung2016/article/details/71774496

		tableView.getColumns().addAll(firstColumn, secondColumn, thirdColumn, fourColumn);
		ArrayList<EXTINF> arrayList = new ArrayList<EXTINF>();
		for (int i = 0; i < 10; i++) {
			EXTINF progressContainer = new EXTINF();
			progressContainer.setDir("dir" + i);
			arrayList.add(progressContainer);
		}

		ObservableList<EXTINF> observableArrayList = FXCollections.observableArrayList(arrayList);
		tableView.setItems(observableArrayList);
		pane.add(tableView, 0, 2, 2, 1);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void showStuTable(ObservableList<EXTINF> stuLists) {
		TableColumn<EXTINF, String> idCol = new TableColumn<EXTINF, String>();
		idCol.setCellFactory((col) -> {
			TableCell<EXTINF, String> cell = new TableCell<EXTINF, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					this.setText(null);
					this.setGraphic(null);

					if (!empty) {
						int rowIndex = this.getIndex() + 1;
						this.setText(String.valueOf(rowIndex));
					}
				}
			};
			return cell;
		});
	}

	private void initEventHandler() {
		dirTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if (KeyCode.ENTER == keyCode) {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					directoryChooser.setTitle("选择文件夹");
					File dir = directoryChooser.showDialog(primaryStage);
					if (null != dir) {
						dirTextField.setText(dir.getAbsolutePath());
					}

				}

			}
		});
		dirTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (2 == event.getClickCount()) {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					directoryChooser.setTitle("选择文件夹");
					File dir = directoryChooser.showDialog(primaryStage);
					if (null != dir) {
						dirTextField.setText(dir.getAbsolutePath());
					}
				}

			}
		});

		downloadButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				String downloadUrl = urlTextField.getText();
				String dir = dirTextField.getText();
				Image image = CommonUtility.getImage("title.png");
				if (null == downloadUrl || "".equals(downloadUrl.trim())) {
					Alert urlAlert = new Alert(AlertType.ERROR);
					Stage window = (Stage) urlAlert.getDialogPane().getScene().getWindow();
					window.getIcons().add(image);
					urlAlert.setHeaderText("下载链接不能为空!");
					urlAlert.setTitle("提示");
					urlAlert.show();
				} else if (null == dir || "".equals(dir.trim())) {
					Alert urlAlert = new Alert(AlertType.ERROR);
					Stage window = (Stage) urlAlert.getDialogPane().getScene().getWindow();
					window.getIcons().add(image);
					urlAlert.setHeaderText("保存路径不能为空!");
					urlAlert.setTitle("提示");
					urlAlert.show();
				}

				ProgressContainer progressContainer = new ProgressContainer(downloadUrl, dir);
				addProgressContainer(progressContainer);
				if (null != downloadUrl && !downloadUrl.isEmpty() && null != dir && !dir.isEmpty()) {
					// 启动下载
					progressContainer.download();
				}
			}
		});
	}

	public void addProgressContainer(ProgressContainer progressContainer) {
		GridPane.setHalignment(progressContainer.getLabel(), HPos.RIGHT);
		++rowIndex;
		gridPane.add(progressContainer.getLabel(), 0, rowIndex);
		gridPane.add(progressContainer.gethBox(), 1, rowIndex);
	}

	public void remove(int index) {

//GridPane p=new GridPane();
//p.getChildren().clear();        //清空面板
//p.getChildren().remove(int index);   //根据下标去除结点
//p.getChildren().remove(Node );        //去除node结点
//p.getChildren().remove(int form,int to);  //根据范围去除结点
//p.getChildren().removeAll(Node...elements) //根据一个Node组去除结点 
		gridPane.getChildren().remove(index);
	}

}
