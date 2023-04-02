package net.rickiekarp.core.view

import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Stage
import net.rickiekarp.core.AppContext
import net.rickiekarp.core.controller.LanguageController
import net.rickiekarp.core.debug.DebugHelper
import net.rickiekarp.core.debug.ExceptionHandler
import net.rickiekarp.core.ui.windowmanager.ImageLoader
import net.rickiekarp.core.ui.windowmanager.WindowScene
import net.rickiekarp.core.ui.windowmanager.WindowStage
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

/**
 * About Stage GUI.
 */
class AboutScene {
    private var grid: GridPane? = null
    private var grid2: GridPane? = null
    private var controls: HBox? = null
    private val WEBSITE = "www.rickiekarp.net"


    private//padding top, left, bottom, right
    //padding top, left, bottom, right
    //grid2.setMaxWidth(250);
    //add Grids to VBox Layout
    //add components
    //padding top, left, bottom, right
    //add vbox & controls pane to borderpane layout
    //TODO: fix the urlBtn for Linux systems
    val content: BorderPane
        get() {

            val borderpane = BorderPane()
            borderpane.style = "-fx-background-color: #1d1d1d;"

            val hbox = HBox()

            grid = GridPane()
            grid2 = GridPane()
            controls = HBox()

            hbox.alignment = Pos.CENTER_LEFT

            val separator2 = Separator()
            separator2.orientation = Orientation.VERTICAL
            separator2.maxHeight = 160.0
            separator2.padding = Insets(0.0, 0.0, 0.0, 0.0)
            if (DebugHelper.isDebugVersion) {
                separator2.style = "-fx-background-color: red;"
            }

            grid!!.vgap = 8.0
            grid!!.padding = Insets(20.0, 15.0, 0.0, 20.0)
            grid!!.minWidth = 180.0

            grid2!!.vgap = 20.0
            grid2!!.padding = Insets(20.0, 15.0, 0.0, 20.0)

            HBox.setHgrow(grid2, Priority.ALWAYS)
            hbox.children.add(0, grid)
            hbox.children.add(1, separator2)
            hbox.children.add(2, grid2)
            val title = Label(AppContext.context.applicationName)
            title.style = "-fx-font-size: 16pt;"
            GridPane.setHalignment(title, HPos.CENTER)
            GridPane.setConstraints(title, 0, 0)
            grid!!.children.add(title)

            val logo = ImageView(ImageLoader.getAppIcon())
            logo.fitHeightProperty().setValue(60)
            logo.fitWidthProperty().setValue(60)
            GridPane.setHalignment(logo, HPos.CENTER)
            GridPane.setConstraints(logo, 0, 1)
            grid!!.children.add(logo)

            val version = Label(AppContext.context.versionNumber)
            version.style = "-fx-font-size: 11pt;"
            GridPane.setHalignment(version, HPos.CENTER)
            GridPane.setConstraints(version, 0, 2)
            grid!!.children.add(version)

            val description = Label(LanguageController.getString("desc"))
            description.setMaxSize(350.0, 200.0)
            description.isWrapText = true
            GridPane.setConstraints(description, 0, 0)
            grid2!!.children.add(description)

            val copyright = Label(LanguageController.getString("copyright"))
            copyright.style = "-fx-font-size: 10pt;"
            GridPane.setConstraints(copyright, 0, 1)
            grid2!!.children.add(copyright)

            val clBtn = Button(LanguageController.getString("changelog"))
            controls!!.children.add(clBtn)

            val urlBtn = Button(LanguageController.getString("website"))
            controls!!.children.add(urlBtn)

            controls!!.padding = Insets(10.0, 7.0, 10.0, 7.0)
            controls!!.spacing = 10.0
            controls!!.alignment = Pos.CENTER_RIGHT
            borderpane.center = hbox
            borderpane.bottom = controls

            clBtn.setOnAction { e -> ChangelogScene() }
            urlBtn.setOnAction { e ->
                try {
                    Desktop.getDesktop().browse(URI(WEBSITE))
                } catch (e1: IOException) {
                    if (DebugHelper.DEBUGVERSION) {
                        e1.printStackTrace()
                    } else {
                        ExceptionHandler(e1)
                    }
                } catch (e1: URISyntaxException) {
                    if (DebugHelper.DEBUGVERSION) {
                        e1.printStackTrace()
                    } else {
                        ExceptionHandler(e1)
                    }
                }
            }

            return borderpane
        }

    init {
        create()
    }

    private fun create() {
        val infoStage = Stage()
        infoStage.title = LanguageController.getString("about") + " " + AppContext.context.applicationName
        infoStage.icons.add(ImageLoader.getAppIconSmall())
        infoStage.isResizable = false
        //infoStage.setMinWidth(500); infoStage.setMinHeight(320);
        infoStage.width = 500.0
        infoStage.height = 320.0

        val contentVbox = BorderPane()

        // The UI (Client Area) to display
        contentVbox.center = content

        // The Window as a Scene
        val aboutWindow = WindowScene(WindowStage("about", infoStage), contentVbox, 1)

        infoStage.scene = aboutWindow
        infoStage.show()

        debugAbout()

        MainScene.stageStack.push(WindowStage("about", infoStage))
    }

    private fun debugAbout() {
        if (DebugHelper.isDebugVersion) {
            grid!!.isGridLinesVisible = true
            grid!!.style = "-fx-background-color: #333333;"
            grid2!!.isGridLinesVisible = true
            grid2!!.style = "-fx-background-color: #444444;"
            controls!!.style = "-fx-background-color: #336699;"
        } else {
            grid!!.isGridLinesVisible = false
            grid!!.style = null
            grid2!!.isGridLinesVisible = false
            grid2!!.style = null
            controls!!.style = null
        }
    }
}