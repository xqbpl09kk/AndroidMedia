package me.pl09kk.learn.android.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_main.view.*

class MainActivity : AppCompatActivity() {

    private val itemsTitle = arrayOf(
        "1、绘制图片",
        "2、录制播放音视频",
        "3、预览camera",
        "4、解析、封装mp4",
        "5、OpenGL入门",
        "6、OpenGL纹理绘制",
        "7、MediaCodec音频硬编、硬解",
        "8、MediaCodec API，完成视频 H.264 的硬编、硬解",
        "9、音视频的采集、编码、封包成mp4输出",
        "10、mp4的解析、音视频的解码、播放和渲染",
        "11、视频的剪裁、旋转、水印、滤镜，并学习 OpenGL 高级特性，如：VBO，VAO，FBO",
        "12、Android 图形图像架构，使用 GLSurfaceviw 绘制 Camera 预览画面",
        "13 深入研究音视频相关的网络协议，如 rtmp，hls，以及封包格式，如：flv，mp4",
        "14 深入学习一些音视频领域的开源项目，如 webrtc，ffmpeg，ijkplayer，librtmp 等等",
        "15 将 ffmpeg 库移植到 Android 平台，结合上面积累的经验，编写一款简易的音视频播放器",
        "16 将 x264 库移植到 Android 平台，结合上面积累的经验，完成视频数据 H264 软编功能",
        "17 将 librtmp 库移植到 Android 平台，结合上面积累的经验，完成 Android RTMP 推流功能",
        "18 上面积累的经验，做一款短视频 APP，完成如：断点拍摄、添加水印、本地转码、视频剪辑、视频拼接、MV 特效等功能"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerViews()
    }


    private fun registerViews() {
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            private val inflater = LayoutInflater.from(MyApp.instance)
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(
                    inflater.inflate(R.layout.list_item_main, parent, false)
                ) {
                    init {
                        itemView.setOnClickListener {
                            when (adapterPosition) {
                                0 -> {

                                }
                            }
                            Toast.makeText(
                                MyApp.instance,
                                "Clicked $adapterPosition",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return itemsTitle.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.titleText.text = itemsTitle[position]
            }

        }
        recyclerView.layoutManager = LinearLayoutManager(MyApp.instance)
    }
}