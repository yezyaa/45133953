document.addEventListener('DOMContentLoaded', function () {
    const boardId = new URLSearchParams(window.location.search).get('boardId'); // URL에서 boardId 가져오기
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardAuthor = document.getElementById('boardAuthor');
    const boardCreatedAt = document.getElementById('boardCreatedAt');
    const boardViews = document.getElementById('boardViews');
    const attachmentList = document.getElementById('attachmentList');
    const backToListButton = document.getElementById('backToListButton');
    const editButton = document.getElementById('editButton');
    const cancelEditButton = document.getElementById('cancelEditButton');
    const saveEditButton = document.getElementById('saveEditButton');
    const buttonContainer = document.getElementById('buttonContainer');
    const editButtonContainer = document.getElementById('editButtonContainer');

    if (!boardId) {
        alert('게시글 ID가 유효하지 않습니다.');
        window.location.href = '/boardList.html';
        return;
    }

    // 게시글 상세 조회 API 호출
    fetch(`/api/board/${boardId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글 정보를 불러오는 데 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            renderBoardDetail(data.data);
        })
        .catch(error => {
            console.error('게시글 상세 조회 에러:', error.message);
            alert(`게시글 상세 정보를 가져오는데 실패했습니다: ${error.message}`);
        });

    // 게시글 상세 정보를 화면에 렌더링
    function renderBoardDetail(board) {
        boardTitle.textContent = board.title;
        boardContent.textContent = board.content;
        boardAuthor.textContent = board.email;
        boardCreatedAt.textContent = new Date(board.createdAt).toLocaleString();
        boardViews.textContent = board.views;

        if (board.attachments && board.attachments.length > 0) {
            board.attachments.forEach(attachment => {
                const listItem = document.createElement('li');
                listItem.textContent = attachment.fileName;
                attachmentList.appendChild(listItem);
            });
        } else {
            const noAttachments = document.createElement('li');
            noAttachments.textContent = '첨부파일이 없습니다.';
            attachmentList.appendChild(noAttachments);
        }
    }

    // 수정 버튼 클릭
    editButton.addEventListener('click', function () {
        buttonContainer.style.display = 'none'; // 기존 버튼 숨김
        editButtonContainer.style.display = 'block'; // 수정 버튼 표시
    });

    // 취소 버튼 클릭
    cancelEditButton.addEventListener('click', function () {
        editButtonContainer.style.display = 'none'; // 수정 버튼 숨김
        buttonContainer.style.display = 'block'; // 기존 버튼 표시
    });

    // 저장 버튼 클릭
    saveEditButton.addEventListener('click', function () {

    });

    // 목록으로 돌아가기
    backToListButton.addEventListener('click', function () {
        window.location.href = '/boardList.html';
    });
});
