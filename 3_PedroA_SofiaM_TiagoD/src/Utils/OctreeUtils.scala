package Utils

import Main.{OcEmpty, OcLeaf, OcNode, Octree}
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Box, DrawMode, Shape3D}
import javafx.scene.{Group, Node}

import scala.annotation.tailrec

object OctreeUtils {
  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size

  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  val redMaterial = new PhongMaterial(Color.rgb(150, 0, 0))

  val greenMaterial = new PhongMaterial(Color.rgb(0, 255, 0))

  val blueMaterial = new PhongMaterial(Color.rgb(0, 0, 150))


  def scaleObjs(fact: Double, objs: List[Node]): List[Node] = (objs foldRight List[Node]()) ((x, lst) => {
    x.setTranslateX(x.getTranslateX * fact)
    x.setTranslateY(x.getTranslateY * fact)
    x.setTranslateZ(x.getTranslateZ * fact)
    x.setScaleX(x.getScaleX * fact)
    x.setScaleY(x.getScaleY * fact)
    x.setScaleZ(x.getScaleZ * fact)
    x :: lst
  })

  def scaleLeaf(fact: Double, oldSec: Section): Section = {
    (((oldSec._1._1._1 * fact, oldSec._1._1._2 * fact, oldSec._1._1._3 * fact), oldSec._1._2 * fact), scaleObjs(fact, oldSec._2))
  }


  def scaleOctree(fact: => Double)(oct: Octree[Placement]): Octree[Placement] = scaleOc(fact)(oct)

  def scaleOc(fact: Double)(oct: Octree[Placement]): Octree[Placement] = oct match {
    case OcEmpty => OcEmpty
    case OcLeaf(section) => OcLeaf(scaleLeaf(fact, section.asInstanceOf[Section]))
    case OcNode(coords, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
      OcNode(((coords._1._1 * fact, coords._1._2 * fact, coords._1._3 * fact), coords._2 * fact),
        scaleOctree(fact)(up_00), scaleOctree(fact)(up_01), //rever
        scaleOctree(fact)(up_10), scaleOctree(fact)(up_11),
        scaleOctree(fact)(down_00), scaleOctree(fact)(down_01),
        scaleOctree(fact)(down_10), scaleOctree(fact)(down_11))
  }

  def partitionColor(partition: Shape3D, cam: Shape3D): Unit = {
    if (partition.getBoundsInParent.intersects(cam.getBoundsInParent))
      partition.setMaterial(greenMaterial)
    else partition.setMaterial(redMaterial)
  }

  def identifyPartitions(lst: List[Node], cam: Shape3D): Any = lst map (x => partitionColor(x.asInstanceOf[Shape3D], cam))

  implicit val boo: Boolean = false

  def createBox(d: Double, d1: Double, d2: Double, d3: Double)(implicit boo: Boolean): Node = {
    if (boo)
      println("size: " + d + " X: " + d1 + " Y: " + d2 + " Z: " + d3)
    val newB = new Box(d, d, d)
    newB.setTranslateX(d / 2 + d1)
    newB.setTranslateY(d / 2 + d2)
    newB.setTranslateZ(d / 2 + d3)
    newB.setMaterial(redMaterial)
    newB.setDrawMode(DrawMode.LINE)
    newB
  }

  def createSubPartitionList(n: Double, plac: Placement): List[Node] = {
    val list0 = Nil :+ createBox(n, d1 = plac._1._1, d2 = plac._1._2, d3 = plac._1._3)
    val list1 = list0 :+ createBox(n, d1 = plac._1._1, d2 = plac._1._2, d3 = plac._1._3 + n)
    val list2 = list1 :+ createBox(n, d1 = plac._1._1 + n, d2 = plac._1._2, d3 = plac._1._3)
    val list3 = list2 :+ createBox(n, d1 = plac._1._1 + n, d2 = plac._1._2, d3 = plac._1._3 + n)
    val list4 = list3 :+ createBox(n, d1 = plac._1._1, d2 = plac._1._2 + n, d3 = plac._1._3)
    val list5 = list4 :+ createBox(n, d1 = plac._1._1, d2 = plac._1._2 + n, d3 = plac._1._3 + n)
    val list6 = list5 :+ createBox(n, d1 = plac._1._1 + n, d2 = plac._1._2 + n, d3 = plac._1._3)
    list6 :+ createBox(n, d1 = plac._1._1 + n, d2 = plac._1._2 + n, d3 = plac._1._3 + n)
  }

  def getOctree(tree: Octree[Placement], op: Int, plac: Placement): Octree[Placement] = {
    if (op == 7)
      OcNode(plac, tree, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
    else if (op == 6)
      OcNode(plac, OcEmpty, tree, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
    else if (op == 5)
      OcNode(plac, OcEmpty, OcEmpty, tree, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
    else if (op == 4)
      OcNode(plac, OcEmpty, OcEmpty, OcEmpty, tree, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
    else if (op == 3)
      OcNode(plac, OcEmpty, OcEmpty, OcEmpty, OcEmpty, tree, OcEmpty, OcEmpty, OcEmpty)
    else if (op == 2)
      OcNode(plac, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, tree, OcEmpty, OcEmpty)
    else if (op == 1)
      OcNode(plac, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, tree, OcEmpty)
    else if (op == 0)
      OcNode(plac, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, tree)
    else
      throw new IllegalArgumentException("?")
  }

  def insertInNewTree(obj: Node, curPartObj: Node, curSize: Size, depth: Int): Octree[Placement] = {
    val newDepth = depth - 1
    val n = curSize / 2
    val plac: Placement = ((curPartObj.getTranslateX - n, curPartObj.getTranslateY - n, curPartObj.getTranslateZ - n), curSize)

    @tailrec
    def loop(subPartitions: List[Node]): Octree[Placement] = subPartitions match {
      case Nil => OcLeaf(plac, List(obj))
      case h :: t =>
        if (h.getBoundsInParent.contains(obj.asInstanceOf[Shape3D].getBoundsInParent) && newDepth > 0)
          getOctree(insertInNewTree(obj, h, n, newDepth), t.length, plac)
        else
          loop(t)
    }

    val subPartitions = createSubPartitionList(n, plac)
    loop(subPartitions)
  }

  //TODO tecnicamente os placs permitem saber o n e o if correto. Por agora optamos continuar com o n e o op
  def chooseAndGetOctree(obj: Node, oc: Octree[Placement], op: Int, subPlac: Placement, depth: Int): Octree[Placement] = {
    val OcNode(plac, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) = oc

    if (op == 7)
      OcNode(plac, insertInTree(obj, up_00, subPlac, depth), up_01, up_10, up_11, down_00, down_01, down_10, down_11)
    else if (op == 6)
      OcNode(plac, up_00, insertInTree(obj, up_01, subPlac, depth), up_10, up_11, down_00, down_01, down_10, down_11)
    else if (op == 5)
      OcNode(plac, up_00, up_01, insertInTree(obj, up_10, subPlac, depth), up_11, down_00, down_01, down_10, down_11)
    else if (op == 4)
      OcNode(plac, up_00, up_01, up_10, insertInTree(obj, up_11, subPlac, depth), down_00, down_01, down_10, down_11)
    else if (op == 3)
      OcNode(plac, up_00, up_01, up_10, up_11, insertInTree(obj, down_00, subPlac, depth), down_01, down_10, down_11)
    else if (op == 2)
      OcNode(plac, up_00, up_01, up_10, up_11, down_00, insertInTree(obj, down_01, subPlac, depth), down_10, down_11)
    else if (op == 1)
      OcNode(plac, up_00, up_01, up_10, up_11, down_00, down_01, insertInTree(obj, down_10, subPlac, depth), down_11)
    else if (op == 0)
      OcNode(plac, up_00, up_01, up_10, up_11, down_00, down_01, down_10, insertInTree(obj, down_11, subPlac, depth))
    else
      throw new IllegalArgumentException("?")
  }

  def insertInTree(obj: Node, ocAndPartis: Octree[Placement], plac: Placement, depth: Int): Octree[Placement]
  = ocAndPartis match {
    case OcEmpty => insertInNewTree(obj, createBox(plac._2, d1 = plac._1._1, d2 = plac._1._2, d3 = plac._1._3), plac._2, depth) //TODO podemos usar um node em vez do plac (tbm podemos trocar todos os node por plac ?)
    case OcLeaf((plac, list)) => OcLeaf(plac, obj :: list.asInstanceOf[List[Node]])
    case OcNode(plac, _, _, _, _, _, _, _, _) =>
      val newDepth = depth - 1
      val n = plac._2 / 2

      @tailrec
      def loop(subPartitions: List[Node]): Octree[Placement] = subPartitions match {
        case Nil => OcLeaf(plac, obj :: getTreeObjsList(ocAndPartis))
        case h :: t =>
          if (h.getBoundsInParent.contains(obj.asInstanceOf[Shape3D].getBoundsInParent) && newDepth > 0)
            chooseAndGetOctree(obj, ocAndPartis, t.length, ((h.getTranslateX - n / 2, h.getTranslateY - n / 2, h.getTranslateZ - n / 2), n), newDepth)
          else
            loop(t)
      }

      val subPartitions = createSubPartitionList(n, plac)
      loop(subPartitions)
  }

  def getTreeObjsList(oc: Octree[Placement]): List[Node] = oc match {
    case OcEmpty => Nil
    case OcLeaf((_, lst)) => lst.asInstanceOf[List[Node]]
    case OcNode(_, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
      getTreeObjsList(up_00) ++ getTreeObjsList(up_01) ++
        getTreeObjsList(up_10) ++ getTreeObjsList(up_11) ++
        getTreeObjsList(down_00) ++ getTreeObjsList(down_01) ++
        getTreeObjsList(down_10) ++ getTreeObjsList(down_11)
  }

  def createPartitionsList(oc: Octree[Placement])(wR: Group): List[Node] = oc match {
    case OcEmpty => Nil
    case OcLeaf((plac, _)) => val coords = plac.asInstanceOf[Placement]
      val partition = createBox(coords._2, coords._1._1, coords._1._2, coords._1._3)(boo = false)
      wR.getChildren.add(partition)
      List(partition)
    case OcNode(coords, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
      val partition = createBox(coords._2, coords._1._1, coords._1._2, coords._1._3)(boo = false)
      wR.getChildren.add(partition)
      partition ::
        createPartitionsList(up_00)(wR) ++ createPartitionsList(up_01)(wR) ++
          createPartitionsList(up_10)(wR) ++ createPartitionsList(up_11)(wR) ++
          createPartitionsList(down_00)(wR) ++ createPartitionsList(down_01)(wR) ++
          createPartitionsList(down_10)(wR) ++ createPartitionsList(down_11)(wR)
  }

  def createOctree(worldRoot: Group, depth: Int): (Octree[Placement], List[Node]) = {
    @tailrec
    def loop(objs: List[Node])(ocAndPartis: Octree[Placement]): Octree[Placement] = objs match {
      case Nil => ocAndPartis
      case h :: t =>
        loop(t)(insertInTree(h, ocAndPartis, plac = ((0, 0, 0), 32), depth))
    }

    val list = worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]]
    val dropedList = list drop 5
    val oct = loop(dropedList)(OcEmpty)
    val lst = createPartitionsList(oct)(worldRoot)
    (oct, lst)
  }


  def mapColourEffect(func: Color => Color)(oct: Octree[Placement]): Octree[Placement] = oct match {
    case OcEmpty => OcEmpty
    case OcLeaf((plac, lst)) => OcLeaf(plac, lst.asInstanceOf[List[Node]] map (obj => {

      val oldColor = obj.asInstanceOf[Shape3D].getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
      val newColor = func(oldColor)
      val newMaterial = new PhongMaterial(newColor)
      obj.asInstanceOf[Shape3D].setMaterial(newMaterial)
      obj
    }
      ))
    case OcNode(plac, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
      OcNode(plac,
        mapColourEffect(func)(up_00), mapColourEffect(func)(up_01),
        mapColourEffect(func)(up_10), mapColourEffect(func)(up_11),
        mapColourEffect(func)(down_00), mapColourEffect(func)(down_01),
        mapColourEffect(func)(down_10), mapColourEffect(func)(down_11))
  }


}
