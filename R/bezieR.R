draw.one.bezier <- function(x1, x2, x3, x4, y1, y2, y3, y4, npts = 10,new = TRUE, ...) {
    pts = one.bezier(x1, x2, x3, x4, y1, y2, y3, y4, npts = npts)
    if (new)
        plot(pts, type = "n",
             xlim = c(min(pts$x, x1, x2, x3, x4),max(pts$x, x1, x2, x3, x4)),
             ylim = c(min(pts$y, y1, y2, y3, y4),max(pts$y, y1, y2, y3, y4)),...)
    lines(pts, ...)
    points(c(x1, x4), c(y1, y4), pch = 20, ...)
    points(c(x2, x3), c(y2, y3), pch = 10, ...)
    lines(c(x1, x2), c(y1, y2), lty = 3)
    lines(c(x4, x3), c(y4, y3), lty = 3)
    invisible(pts)
}

draw.beziers <- function(x1, x2, x3, x4, y1, y2, y3, y4, npts = 10,new = TRUE, ...) {
    allx = c(x1, x2, x3, x4)
    ally = c(y1, y2, y3, y4)
    xlim = c(min(allx), max(allx))
    ylim = c(min(ally), max(ally))
    if (new)
        plot(-1:2, xlim = xlim, ylim = ylim,...,col='white')
    xpts = c()
    ypts = c()
    for (k in 1:length(x1)) {
        pts = draw.one.bezier(x1[k], x2[k], x3[k], x4[k], y1[k],
                              y2[k], y3[k], y4[k], npts=npts,new = FALSE, xlim = xlim, ylim = ylim,
                              ...)
        xpts = c(xpts, pts$x)
        ypts = c(ypts, pts$y)
    }
    invisible(list(x = xpts, y = ypts))
}

plot_curve <- function(T, npts = 100, ...) {
    draw.beziers(T[, 1], T[, 3], T[, 5], T[, 7], T[, 2],T[, 4], T[, 6], T[, 8],npts=npts,xlab="",ylab="")
}

draw_beziers <- function(window_size = 800) {
    class_name <- J("BezierDraw")
    drawer <- new(class_name)
    .jcall(drawer, "V", "drawer")
}
