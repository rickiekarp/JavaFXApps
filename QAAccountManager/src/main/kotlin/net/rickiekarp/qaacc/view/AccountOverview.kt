package net.rickiekarp.qaacc.view

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import net.rickiekarp.core.debug.DebugHelper
import net.rickiekarp.core.debug.ExceptionHandler
import net.rickiekarp.core.debug.LogFileHandler
import net.rickiekarp.core.provider.LocalizationProvider
import net.rickiekarp.core.ui.windowmanager.ImageLoader
import net.rickiekarp.core.ui.windowmanager.WindowScene
import net.rickiekarp.core.ui.windowmanager.WindowStage
import net.rickiekarp.qaacc.factory.AccountXmlFactory
import net.rickiekarp.qaacc.model.Account
import net.rickiekarp.qaacc.settings.AppConfiguration
import java.net.MalformedURLException
import java.util.logging.Level

class AccountOverview(projectID: Int) {
    var accOverviewScene: WindowScene? = null
        private set

    init {
        val accOverview = overview
        if (accOverview == null) {
            overview = this
            create(projectID)
        } else {
            if (accOverview.accOverviewScene!!.win.windowStage.stage.isShowing) {
                accOverview.accOverviewScene!!.win.windowStage.stage.requestFocus()
            } else {
                overview = this
                create(projectID)
            }
        }
    }

    private fun create(projectID: Int) {
        val overviewStage = Stage()
        overviewStage.title = AppConfiguration.projectData[projectID].getProjectName() + " - " + LocalizationProvider.getString("account_manager")
        overviewStage.icons.add(ImageLoader.getAppIcon())
        overviewStage.isResizable = true
        overviewStage.width = 770.0
        overviewStage.height = 450.0
        overviewStage.minWidth = 750.0
        overviewStage.minHeight = 430.0

        val borderpane = BorderPane()
        val contentNode = getLayout(projectID)

        // The UI (Client Area) to display
        borderpane.center = contentNode

        accOverviewScene = WindowScene(WindowStage("overview", overviewStage), borderpane, 1)

        overviewStage.scene = accOverviewScene
        overviewStage.show()
        //if (DebugHelper.isDebugVersion()) { DebugHelper.debugAccOverview(); }
        LogFileHandler.logger.log(Level.INFO, "open.AccountOverview{$projectID}")
    }


    private fun getLayout(projectID: Int): Node {
        val mainContent = BorderPane()
        mainContent.styleClass.add("background")

        val splitPane = SplitPane()
        val anchor1 = AnchorPane()
        val anchor2 = AnchorPane()

        splitPane.items.add(0, anchor1)
        splitPane.items.add(1, anchor2)
        splitPane.setDividerPositions(0.35)

        val gridpane = GridPane()
        gridpane.alignment = Pos.BASELINE_LEFT
        gridpane.vgap = 15.0
        gridpane.hgap = 25.0

        val controls = HBox()
        controls.padding = Insets(15.0, 12.0, 15.0, 12.0)  //padding top, left, bottom, right
        controls.spacing = 10.0
        controls.alignment = Pos.CENTER_RIGHT

        tableview = TableView()
        tableview.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        tableview.placeholder = Label(LocalizationProvider.getString("no_account_found"))


        val column1 = TableColumn<Account, Any>(LocalizationProvider.getString("name"))
        column1.setCellValueFactory(PropertyValueFactory("accName"))
        val column2 = TableColumn<Account, Any>(LocalizationProvider.getString("mail"))
        column2.isVisible = false
        column2.setCellValueFactory(PropertyValueFactory("accMail"))
        val column3 = TableColumn<Account, Any>(LocalizationProvider.getString("level"))
        column3.isVisible = false
        column3.setCellValueFactory(PropertyValueFactory("accLevel"))
        val column4 = TableColumn<Account, Any>()
        column4.isVisible = false
        column4.setCellValueFactory(PropertyValueFactory("accAlliance"))

        AnchorPane.setTopAnchor(tableview, 0.0)
        AnchorPane.setRightAnchor(tableview, 0.0)
        AnchorPane.setBottomAnchor(tableview, 0.0)
        AnchorPane.setLeftAnchor(tableview, 0.0)

        AnchorPane.setTopAnchor(gridpane, 75.0)
        AnchorPane.setLeftAnchor(gridpane, 15.0)
        AnchorPane.setRightAnchor(gridpane, 15.0)

        AnchorPane.setRightAnchor(controls, 10.0)
        AnchorPane.setBottomAnchor(controls, 10.0)

        tableview.setItems(AppConfiguration.accountData)
        tableview.tableMenuButtonVisibleProperty().set(true)
        tableview.columns.setAll(column1, column2, column3, column4)


        //add components
        val header = Label("Account Details:")
        header.styleClass.add("label-header")
        anchor2.children.add(0, header)
        AnchorPane.setTopAnchor(header, 5.0)
        AnchorPane.setLeftAnchor(header, 10.0)

        val nameL = Label(LocalizationProvider.getString("name"))
        GridPane.setConstraints(nameL, 0, 0)
        gridpane.children.add(nameL)

        nameTF = Label()
        GridPane.setConstraints(nameTF, 1, 0)
        gridpane.children.add(nameTF)

        val mailL = Label(LocalizationProvider.getString("mail"))
        GridPane.setConstraints(mailL, 0, 1)
        gridpane.children.add(mailL)

        mailTF = Label()
        mailTF.minWidth = 300.0
        GridPane.setConstraints(mailTF, 1, 1)
        gridpane.children.add(mailTF)

        val lvL = Label(LocalizationProvider.getString("level"))
        GridPane.setConstraints(lvL, 0, 2)
        gridpane.children.add(lvL)

        lvTF = Label()
        GridPane.setConstraints(lvTF, 1, 2)
        GridPane.setFillWidth(lvTF, false)
        gridpane.children.add(lvTF)

        val alliL = Label()
        GridPane.setConstraints(alliL, 0, 3)
        gridpane.children.add(alliL)

        alliTF = Label()
        GridPane.setConstraints(alliTF, 1, 3)
        gridpane.children.add(alliTF)

        accCount = Label()
        anchor2.children.add(1, accCount)
        AnchorPane.setBottomAnchor(accCount, 75.0)
        AnchorPane.setRightAnchor(accCount, 250.0)

        status = Label(LocalizationProvider.getString("ready"))
        anchor2.children.add(2, status)
        AnchorPane.setBottomAnchor(status, 75.0)
        AnchorPane.setRightAnchor(status, 60.0)

        val newAcc = Button(LocalizationProvider.getString("create"))
        controls.children.add(newAcc)

        editAcc = Button(LocalizationProvider.getString("edit"))
        controls.children.add(editAcc)

        delAcc = Button(LocalizationProvider.getString("delete"))
        controls.children.add(delAcc)

        //set project specific names
        when (projectID) {
            2 -> {
                alliL.text = LocalizationProvider.getString("cooperative")
                column4.setText(LocalizationProvider.getString("cooperative"))
            }
            else -> {
                alliL.text = LocalizationProvider.getString("alliance")
                column4.setText(LocalizationProvider.getString("alliance"))
            }
        }

        //load all accounts for selected project
        AppConfiguration.accountData.clear()
        AccountXmlFactory.loadAccData(projectID)

        when (AppConfiguration.accountData.size) {
            0 -> {
                editAcc.isDisable = true
                delAcc.isDisable = true
                accCount.text = "0 " + LocalizationProvider.getString("accs_loaded")
            }
            1 -> {
                accCount.text = "1 " + LocalizationProvider.getString("acc_loaded")
                tableview.selectionModel.select(0)
            }
            else -> {
                accCount.text = AppConfiguration.accountData.size.toString() + " " + LocalizationProvider.getString("accs_loaded")
                tableview.selectionModel.select(0)
            }
        }

        //add to AnchorPane Layout
        anchor1.children.add(0, tableview)
        anchor2.children.add(0, gridpane)
        anchor2.children.add(1, controls)

        //add SplitPane to borderpane layout
        mainContent.center = splitPane

        newAcc.setOnAction { _ -> AccountEditDialog(projectID, "new", -1, null) }

        editAcc.setOnAction { _ ->
            val selectedIndex = tableview.selectionModel.focusedIndex
            val selectedAccount = tableview.selectionModel.selectedItem

            AccountEditDialog(projectID, "edit", selectedIndex, selectedAccount)
            Account.showAccountDetails(selectedAccount)
        }

        delAcc.setOnAction { _ ->
            val selectedIndex = tableview.selectionModel.focusedIndex
            try {
                AccountXmlFactory.removeAccountFromXml(projectID, selectedIndex)
                if (selectedIndex >= 0) {
                    AppConfiguration.accountData.removeAt(selectedIndex)
                    tableview.selectionModel.select(selectedIndex)
                }
            } catch (e1: MalformedURLException) {
                if (DebugHelper.DEBUG) {
                    e1.printStackTrace()
                } else {
                    ExceptionHandler(e1)
                }
            }
        }

        //Add change listener
        tableview.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            //Check whether item is selected
            if (tableview.selectionModel.selectedItem != null) {
                editAcc.isDisable = false
                delAcc.isDisable = false
                val selectedAccount = tableview.selectionModel.selectedItem
                nameTF.text = selectedAccount.accName
                mailTF.text = selectedAccount.accMail
                lvTF.text = selectedAccount.accLevel
                alliTF.text = selectedAccount.accAlliance
            } else {
                editAcc.isDisable = true
                delAcc.isDisable = true
            }
        }

        return mainContent
    }

    companion object {
        var overview: AccountOverview? = null
        lateinit var tableview: TableView<Account>

        lateinit var editAcc: Button
        lateinit var delAcc: Button
        lateinit var nameTF: Label
        lateinit var mailTF: Label
        lateinit var alliTF: Label
        lateinit var lvTF: Label
        lateinit var accCount: Label
        lateinit var status: Label

        //refreshes the table
        fun refreshPersonTable(selectedIdx: Int) {
            tableview.items = null
            tableview.layout()
            tableview.items = AppConfiguration.accountData
            tableview.selectionModel.select(selectedIdx)
        }
    }
}