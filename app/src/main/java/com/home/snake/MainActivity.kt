package com.home.snake

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val viewModel = ViewModelProvider(this).get(SnakeViewModel::class.java)
        viewModel.body.observe(this, Observer {
            game_view.snakeBody = it
            game_view.invalidate()

        })
        viewModel.gameState.observe(this, Observer {gameState->
            if(gameState == GameState.GAME_OVER){
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Game")
                        .setMessage("Game Over")
                        .setPositiveButton("OK",null)
                        .setNegativeButton("Replay", DialogInterface.OnClickListener { dialog, which ->
                            viewModel.reset()
                        })
                        .show()
            }
        })
        viewModel.apple.observe(this, Observer {
            game_view.apple = it
            game_view.invalidate()
        })
        viewModel.score.observe(this, Observer {
            count.setText(it.toString())

        })


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Game")
                    .setMessage("Replay?")
                    .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                        viewModel.reset()
                    })
                    .setNegativeButton("no",null)
                    .show()

        }
        viewModel.start()
        top.setOnClickListener { viewModel.move(Direction.TOP )}
        down.setOnClickListener { viewModel.move(Direction.DOWN )}
        left.setOnClickListener { viewModel.move(Direction.LEFT) }
        right.setOnClickListener { viewModel.move(Direction.RIGHT) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_exit -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}