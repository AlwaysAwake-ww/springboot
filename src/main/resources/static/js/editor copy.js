

window.onload = function() {
    // 이전 페이지에서 이동한 경우에만 새로고침을 합니다.
    if (performance.navigation.type == 2) {
        location.reload(true);
    }
}

$(document).ready(function() {
    $('#title').keypress(function(event) {
      if (event.keyCode == 13) {
        event.preventDefault();
      }
    });
  });

const editor = document.getElementById('editor');

const headBtn = document.getElementById('headBtn');
const boldBtn = document.getElementById('boldBtn');
const italicBtn = document.getElementById('italicBtn');
const underlineBtn = document.getElementById('underlineBtn');
const strikeBtn = document.getElementById('strikeBtn');
const orderedListBtn = document.getElementById('orderedListBtn');
const unorderedListBtn = document.getElementById('unorderedListBtn');
const imageBtn = document.getElementById('imageBtn');
const imageSelector = document.getElementById('imageSelector');

const saveBtn = document.getElementById('saveBtn');
const cancelBtn = document.getElementById('cancelBtn');
const previewBtn = document.getElementById('previewBtn');
const saveModalBtn = document.getElementById('saveModalBtn');
const cancelModalBtn = document.getElementById('cancelModalBtn');


const title = document.getElementById('title');
const imageUploadForm = document.getElementById('imageUploadForm');


const imageList = document.getElementById('imageList');
const imageListBody = document.getElementById('imageListBody');

const thumbnail = document.getElementById('thumbnail');
const previewThumbnail = document.getElementById('previewThumbnail');


headBtn.addEventListener('change', function (e) {

    const option = headBtn.options[headBtn.selectedIndex].text;

    setHead(option);
    editor.focus({ preventScroll: true });
    checkStyle();
});

boldBtn.addEventListener('click', function () {
    setStyle('bold');
});
italicBtn.addEventListener('click', function () {
    setStyle('italic');
});
underlineBtn.addEventListener('click', function () {
    setStyle('underline');
});
strikeBtn.addEventListener('click', function () {
    setStyle('strikeThrough');
});
orderedListBtn.addEventListener('click', function () {
    setStyle('insertOrderedList');
});
unorderedListBtn.addEventListener('click', function () {
    setStyle('insertUnorderedList');
});
imageBtn.addEventListener('click', function () {
    imageSelector.click();
});


function setHead(option) {

    if (option == 'Paragraph') {
        document.execCommand('formatBlock', false, '<div>');
    }
    else if (option == 'Heading1') {
        document.execCommand('formatBlock', false, 'H1');
    }
    else if (option == 'Heading2') {
        document.execCommand('formatBlock', false, 'H2');
    }
    else if (option == 'Heading3') {
        document.execCommand('formatBlock', false, 'H3');
    }
    else if (option == 'Heading4') {
        document.execCommand('formatBlock', false, 'H4');
    }
    cancelStyle();
    editor.focus({ preventScroll: true });
    checkStyle();
}

function cancelStyle() {

    if (isStyle('insertOrderedList'))
        document.execCommand('insertOrderedList');

    if (isStyle('insertUnorderedList'))
        document.execCommand('insertUnorderedList');

}


function setStyle(style) {

    document.execCommand(style);
    editor.focus({ preventScroll: true });
    checkStyle();

}


imageSelector.addEventListener('change', function (e) {
    const files = e.target.files;
    if (!!files) {
        insertImageDataUpload(files[0]);
    }
});

function insertImageDataUpload(file) {

    formData = new FormData();
    formData.append('image', file);

    $.ajax({
        url: '/user/write/tempimgupload',
        async: true,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        data: formData,
        method: 'POST'
    })
        .done(function (image) {
            alert('이미지 첨부 성공');
            insertImageData(file, image.originName, image.newName);
            addImageList(image);
        })
        .fail(function (image) {
            alert('image upload fail');

        })
};

function addImageList(image) {


    const row = document.createElement('tr');
    const originName = document.createElement('td');
    const newName = document.createElement('td');

    originName.innerHTML = image.originName;
    newName.innerHTML = image.newName;

    row.appendChild(originName);
    row.appendChild(newName);



    imageListBody.appendChild(row);

};

function deleteImageList(imageName) {


    const rows = imageList.getElementsByTagName('tr');
    for (var i = 0; i < rows.length; i++) {
        const row = rows[i];

        if (row.cells[1].innerHTML== imageName) {
            row.parentNode.removeChild(row);
            return;
        }
    }
};

function insertImageData(file) {
    const reader = new FileReader();
    reader.addEventListener('load', function (e) {
        editor.focus({ preventScroll: true });
        image = reader.result;

        document.execCommand('insertImage', false, `${reader.result}`);
    });

    reader.addEventListener('loadend', function () {
        let images = document.querySelectorAll('img');
        for (element of images)
            element.classList.add('img-fluid');
    });
    reader.readAsDataURL(file);
};


///////////////////////////////////////////////////////
// 이미지 미리보기 삽입 테스트
///////////////////////////////////////////////////////

function insertImageData(file, originName, newName) {
    const reader = new FileReader();

    reader.onload = function (e) {
        const image = new Image();
        image.src = e.target.result;


        const imageTag = document.createElement('img');
        imageTag.src = e.target.result;
        imageTag.id = newName;

        // document.execCommand('insertImage', false, image.src);
        document.execCommand('insertHTML', false, imageTag.outerHTML);

        let matchInfo = {
            originName: originName,
            newName: newName
        };

        image.dataset.matchInfo = JSON.stringify(matchInfo);

    };


    reader.addEventListener('loadend', function () {
        let images = document.querySelectorAll('img');
        for (element of images)
            element.classList.add('img-fluid');
    });
    reader.readAsDataURL(file);
};

////////////////////////////////////////////////////////////



editor.addEventListener('keydown', function () {

    checkStyle();
});

editor.addEventListener('keyup', function () {

    checkStyle();
});

editor.addEventListener('mousedown', function () {

    checkStyle();
});


editor.addEventListener('mouseup', function () {
    checkStyle();
});

function checkStyle() {

    const style = document.queryCommandValue('formatBlock');

    if (style == 'h1') {
        headBtn.options[1].selected = true;
    }
    else if (style == 'h2') {
        headBtn.options[2].selected = true;
    }
    else if (style == 'h3') {
        headBtn.options[3].selected = true;
    }
    else if (style == 'h4') {
        headBtn.options[4].selected = true;
    }
    else if (style == 'div') {
        headBtn.options[0].selected = true;
    }


    boldBtn.checked = isStyle('bold');
    italicBtn.checked = isStyle('italic');
    underlineBtn.checked = isStyle('underline');
    strikeBtn.checked = isStyle('strikeThrough');
    orderedListBtn.checked = isStyle('insertOrderedList');
    unorderedListBtn.checked = isStyle('insertUnorderedList');
};

function isStyle(style) {
    return document.queryCommandState(style);
};

////////////////////////////////////////////////////////////
// 저장, 취소 버튼 (클릭 시 모달창)
///////////////////////////////////////////////////////////


$('#saveBtn').on('click',function(){
    $('#saveModal').modal('show');
});

$('#cancelBtn').on('click',function(){
    $('#cancelModal').modal('show');
});





////////////////////////////////////////////////////////////
// 저장 모달 버튼
///////////////////////////////////////////////////////////

saveModalBtn.addEventListener('click', function () {

    const rows = imageList.rows;
    
    
    const imageOriginNameArr = new Array();
    const imageNewNameArr = new Array();


    
    

    if(rows.length>=1){
        for (i = 1; i < rows.length; i++) {
            imageOriginNameArr.push(rows[i].cells[0].innerHTML);
            imageNewNameArr.push(rows[i].cells[1].innerHTML);
        }
    }


    $.ajax({
        url: '/user/write/submit',
        async: true,
        data: {
            title: title.value,
            imageOriginNameArr:imageOriginNameArr || null,
            imageNewNameArr: imageNewNameArr || null,
            content: replaceImgWithId(editor)
        },
        method: 'POST'
    })
        .done(function (text) {
            
            if(text=='success'){
                window.location.href='/user/mypost';
            }
            else
                alert(text);
        })
        .fail(function (text) {
            alert('ajax fail');
        });

});


////////////////////////////////////////////////////////////
// 취소 버튼
///////////////////////////////////////////////////////////
cancelModalBtn.addEventListener('click', function () {
    
    window.location.href='/user/mypost';

});


function imgTagRepalce(content) {

    const replaceString = '<img';
    const result = replaceImgTagsWithCount(content, replaceString);

    return result;
};


function replaceImgWithId(editor) {
    const imgs = editor.getElementsByTagName('img');

    for (var i = 0; i < imgs.length; i++) {
        const img = imgs[i];
        const id = img.getAttribute('id');
        if (id) {
            img.outerHTML = '<img id="' + id + '" src="" class="img-fluid">';
        }
    }
    return editor.innerHTML;
};

function replaceImgTagsWithCount(originalString, replacementString) {
    const imgTagPattern = /<img.*?>/g;
    let count = 0;
    const replacedString = originalString.replace(imgTagPattern, function (match) {
        count++;
        return replacementString + count + '>';
    });
    return replacedString;
};


const observer = new MutationObserver(handleMutations);
const config = { childList: true, subtree: true };

observer.observe(editor, config);


function handleMutations(mutationsList, observer) {
    for (const mutation of mutationsList) {
        // 노드의 자식 노드에 변경이 생긴 경우
        if (mutation.type === 'childList') {
            // 삭제된 노드에서 IMG 태그를 가진 노드를 찾음
            const removedImgNodes = Array.from(mutation.removedNodes).filter(node => node.tagName === 'IMG');
            // 이미지가 삭제된 경우
            if (removedImgNodes.length > 0) {
                for (const removedNode of removedImgNodes) {
                    // 이미지가 삭제되었는지 여부를 판단하여 처리
                    const deleted = isImageDeleted(removedNode);
                    if (deleted) {
                        // 이미지가 삭제된 경우 처리할 내용 작성
                        console.log('이미지 삭제됨');
                    }
                }
            }
        }
    }
};

// 삭제 여부를 판단하는 함수
function isImageDeleted(node) {
    // 삭제된 이미지인지 확인
    const imageId = node.id;
    const imageExists = document.getElementById(imageId) !== null;
    console.log(imageId);
    if(!imageExists){
        deletetImage(imageId);
    }
    
    return !imageExists;
}

function deletetImage(imageName) {

    $.ajax({
        url: '/user/write/tempimgdelete',
        async: true,
        data: {
            imageName: imageName
        },
        method: 'POST'
    })
        .done(function (data) {

            deleteImageList(data);
            console.log('image delete success');
        })
        .fail(function (data) {
            alert('image delete fail');

        });
}


previewBtn.addEventListener('click', function() {

    data = $('#editor').html();


    $('#previewContent').html(data);

    $('#previewModal').modal('show');
});

$('#previewModal').on('hidden.bs.modal', function () {
    $(this).find('modal-content').trigger('reset');
});


///////////////////////////////////////////////////////////
// 엔터키 입력 시 폼 제출 방지
////////////////////////////////////////////////////////
// $('.form-control').on('keydown', function (event) {
//     if (event.keyCode === 13 || event.key === 'Enter') {
//         event.preventDefault();
//         return false;
//     }
// });



//////////////////////////////////////////////////////////
// 썸네일 프리뷰
/////////////////////////////////////////////////////////

$("#thumbnail").on("change", function(e) {
    var thumbnailInput = this;
    if (thumbnailInput.files && thumbnailInput.files[0]) {
      var reader = new FileReader();
      reader.onload = function(e) {
        $("#previewThumbnail").empty();
        $("#previewThumbnail").append(
          '<img src="' + e.target.result + '" class="img-thumbnail">'
        );


      };
      reader.readAsDataURL(thumbnailInput.files[0]);
      insertThumbnailDataUpload(thumbnailInput.files[0]);
    }


    

    
  });

  

  

  function insertThumbnailDataUpload(file) {

    formData = new FormData();
    formData.append('image', file);

    $.ajax({
        url: '/user/write/thumbnailupload',
        async: true,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        data: formData,
        method: 'POST'
    })
        .done(function (image) {
            alert('썸네일 업로드 성공');
            
            
            addImageList(image);
        })
        .fail(function (image) {
            alert('썸네일 업로드 실패');

        })
};