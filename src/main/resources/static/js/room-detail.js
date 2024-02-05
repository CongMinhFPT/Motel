/* 25. Zoom Product Venobox
/*----------------------------------------*/
$('.venobox').venobox({
    spinner:'wave',
    spinColor:'#cb9a00',
});
$('.product-details-images').each(function(){
    var $this = $(this);
    var $thumb = $this.siblings('.product-details-thumbs, .tab-style-left');
    $this.slick({
       arrows: false,
       slidesToShow: 1,
       slidesToScroll: 1,
       autoplay: false,
       autoplaySpeed: 5000,
       dots: false,
       infinite: true,
       centerMode: false,
       centerPadding: 0,
       asNavFor: $thumb,
   });
});
$('.product-details-thumbs').each(function(){
    var $this = $(this);
    var $details = $this.siblings('.product-details-images');
    $this.slick({
       slidesToShow: 4,
       slidesToScroll: 1,
       autoplay: false,
       autoplaySpeed: 5000,
       dots: false,
       infinite: true,
       focusOnSelect: true,
       centerMode: true,
       centerPadding: 0,
       prevArrow: '<span class="slick-prev"><i class="bx bxs-chevron-left" ></i></span>',
       nextArrow: '<span class="slick-next"><i class="bx bxs-chevron-right" ></i></span>',
       asNavFor: $details,
   });
});
$(".product-active").owlCarousel({
    loop: true,
    nav: true,
    dots: false,
    autoplay: false,
    autoplayTimeout: 5000,
    navText: ["<i class='bx bx-chevron-left'></i>", "<i class='bx bx-chevron-right'></i>"],
    item: 3,
    responsive: {
        0: {
                items: 1
        },
        480: {
                items: 2
        },
        768: {
                items: 3
        },
        992: {
                items: 4
        },
        1200: {
                items: 3
        }
    }
});