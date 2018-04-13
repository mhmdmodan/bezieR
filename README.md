# bezieR
### Easily draw bezier curves and import them into R

![bezieR editor](https://mhmdmodan.com/imgs/bezier.PNG)

## Installation:

Run:

```r
require(devtools)
install_github("mhmdmodan/bezieR", args="--no-multiarch")
```
This may not work however due to R/Java architecture differences. In that case, clone the repo, open `bezieR.RProj` in RStudio, and press `CTRL+SHIFT+B` to Install and Restart.

## Usage

```r
draw_beziers(window_size = 800L)
```
Intializes and runs the Java program to draw curves.

```r
get_points()
```

Returns the bezier curves' points as a matrix, with each row as a new curve, indexed as {endpoint, control, control, endpoint}.

```r
plot_curve(T = get_points(), npts = 100, ...)
```

plots the bezier curve matrix.

Click, or just click to add a new handle. Just clicking will spawn the handle with control points at the same coordinates as the bezier endpoint, treating it as a single point.

Click and drag to move handles/endpoints.

Control-click allows you to manipulate individual handles without affecting the opposite control point. It also disallows manipulation of endpoints.

Alt-click to delete points.

Double click the first handle to complete the curve.

Control-Z is undo. There is no redo.

Press "s" to save the current state in a file called "points.set", press "o" to load
