NAME1=pflichtenheft
NAME2=presentation

.PHONY: show clean distclean dev

default dev all: distclean build/$(NAME1).pdf
default dev all: distclean build/$(NAME2).pdf

dev: CONT_FLAG = -pvc

build/%.pdf: %.tex *.sty
	mkdir -p build
	latexmk $(CONT_FLAG) -pdf -g -f -auxdir=build -outdir=build $<

show: build/$(NAME1).pdf
	xdg-open $<

show: build/$(NAME2).pdf
	xdg-open $<

clean:
	rm -f *.tmp *.tui *.log *.tuc *.mp *.bbl *.blg *.fls *.idx *.aux *.out *.fdb_latexmk *.ilg *.ind
	rm -rf build

distclean: clean
	rm -f build/$(NAME1).pdf
	rm -f build/$(NAME2).pdf
