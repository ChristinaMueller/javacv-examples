/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter05

import java.io.File

import opencv2_cookbook.OpenCVUtils._
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgcodecs._
import org.bytedeco.javacpp.opencv_imgproc._

/**
 * Example of detecting edges and corners using morphological filters. Based on section "Detecting edges and
 * corners using morphological filters".
 */
object Ex3EdgesAndCorners extends App {

  // Read input image
  val image = loadAndShowOrExit(new File("data/building2.jpg"), CV_LOAD_IMAGE_GRAYSCALE)

  //  resize(image, image, new Size(), 0.7, 0.7, INTER_LINEAR)

  val morpho = new MorphoFeatures()
  morpho.thresholdValue = 40

  val edges = morpho.getEdges(image)
  show(edges, "Edges")

  morpho.thresholdValue = -1
  val corners = morpho.getCorners(image)
  morphologyEx(corners, corners, MORPH_TOPHAT, new Mat())
  threshold(corners, corners, 35, 255, THRESH_BINARY_INV)
  val cornersOnImage = morpho.drawOnImage(corners, image)
  show(cornersOnImage, "Corners on image")
}


