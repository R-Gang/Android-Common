package com.gang.library.common.utils.notch.callback

/**
 * 标记区分哪些界面必须全屏展示、哪些界面允许显示状态栏。这里提供一种实现方式，让允许显示状态栏的界面 Activity 继承一个接口
 * 允许通过显示状态栏方式适配刘海屏
 */
interface CutoutAdapt