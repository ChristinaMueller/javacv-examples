/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter06


import java.io.File

import opencv2_cookbook.OpenCVUtils._
import org.bytedeco.javacpp.DoublePointer
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgcodecs._
import org.bytedeco.javacpp.opencv_imgproc._


/**
 * The example for section "Applying directional filters to detect edges" in Chapter 6, page 148.
 */
object Ex3DirectionalFilters extends App {

  // Read input image with a salt noise
  val src = loadAndShowOrExit(new File("data/boldt.jpg"), CV_LOAD_IMAGE_GRAYSCALE)

  // Sobel edges in X
  val sobelX = new Mat()
  Sobel(src, sobelX, CV_32F, 1, 0)
  show(to8U(sobelX), "Sobel X")

  // Sobel edges in Y
  val sobelY = new Mat()
  Sobel(src, sobelY, CV_32F, 0, 1)
  show(to8U(sobelY), "Sobel Y")

  // Compute norm of directional images to create Sobel edge image
  val sobel = sobelX.clone()
  magnitude(sobelX, sobelY, sobel)
  show(to8U(sobel), "Sobel1")

  val min = new DoublePointer(1)
  val max = new DoublePointer(1)
  minMaxLoc(sobel, min, max, null, null, new Mat())
  println("Sobel min: " + min.get(0) + ", max: " + max.get(0) + ".")

  // Threshold edges
  // Prepare image for display: extract foreground
  val thresholded = new Mat()
  threshold(sobel, thresholded, 100, 255, CV_THRESH_BINARY_INV)

  // FIXME: There us a crash if trying to display directly
  //   Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 16711426
  //	   at java.awt.image.ComponentColorModel.getRGBComponent(ComponentColorModel.java:903)
  //  show(thresholded, "Thresholded")
  //  save(new File("Ex3DirectionalFilters-thresholded.tif"), thresholded)
  show(to8U(thresholded), "Thresholded")


  /**
   * Helper for computing `cvAbs()` of an image.
   */
  def abs(src: IplImage): IplImage = {
    val dest = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels())
    cvAbsDiffS(src, dest, cvScalar(0))
    dest
  }
}
