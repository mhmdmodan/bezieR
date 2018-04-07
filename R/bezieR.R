.pkgglobalenv <- new.env(parent=emptyenv())

############################################################################
##  Credit to Professors David Shuman and Lori Ziegelmeier of             ##
##  Macalester College for supplying the following 4 functions            ##
##  for plotting bezier curves in R!                                      ##
############################################################################
one.bezier <- function(x1, x2, x3, x4, y1, y2, y3, y4, npts = 10) {
    bx = 3 * (x2 - x1)
    cx = 3 * (x3 - x2) - bx
    dx = x4 - x1 - bx - cx
    by = 3 * (y2 - y1)
    cy = 3 * (y3 - y2) - by
    dy = y4 - y1 - by - cy
    t = seq(0, 1, length = npts)
    x = x1 + t * (bx + t * (cx + dx * t))
    y = y1 + t * (by + t * (cy + dy * t))
    return(list(x = x, y = y))
}

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

plot_curve <- function(T = get_points(), npts = 100, ...) {
    draw.beziers(T[, 1], T[, 3], T[, 5], T[, 7], T[, 2],T[, 4], T[, 6], T[, 8],npts=npts,xlab="",ylab="")
}
# End Macalester's code
##########################################################################
##########################################################################
##########################################################################

draw_beziers <- function(window_size = 800L) {
    class_name <- J("BezierDraw")
    assign("global_win_size", window_size, .pkgglobalenv)
    assign("drawer", new(class_name, as.integer(window_size)), .pkgglobalenv)
}

get_points <- function() {
    if(exists('drawer',envir=.pkgglobalenv)==FALSE){
        stop("You need to draw some bezier curves with draw_beziers() first!")
    }
    out_matrix <- .jcall(.pkgglobalenv$drawer, "[[D", "getPoints")
    to_return <- matrix(nrow = 0, ncol = 8)
    for (row in out_matrix) {
        to_return <- rbind(to_return, .jevalArray(row))
    }
    return(to_return/.pkgglobalenv$global_win_size)
}
