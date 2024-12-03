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
    const deleteButton = document.getElementById('deleteButton');

    let originalTitle = '';
    let originalContent = '';

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
        boardTitle.value = board.title;
        boardContent.value = board.content;
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
        originalTitle = boardTitle.value;
        originalContent = boardContent.value;

        boardTitle.disabled = false; // 인풋 활성화
        boardContent.disabled = false; // 텍스트 영역 활성화

        buttonContainer.style.display = 'none'; // 기존 버튼 숨김
        editButtonContainer.style.display = 'block'; // 수정 버튼 표시
    });

    // 취소 버튼 클릭
    cancelEditButton.addEventListener('click', function () {
        boardTitle.value = originalTitle; // 원래 제목 복원
        boardContent.value = originalContent; // 원래 내용 복원

        boardTitle.disabled = true; // 인풋 비활성화
        boardContent.disabled = true; // 텍스트 영역 비활성화

        editButtonContainer.style.display = 'none'; // 수정 버튼 숨김
        buttonContainer.style.display = 'block'; // 기존 버튼 표시
    });

    // 저장 버튼 클릭
    saveEditButton.addEventListener('click', function () {
        const updatedTitle = boardTitle.value;
        const updatedContent = boardContent.value;

        const formData = new FormData();
        formData.append('title', updatedTitle);
        formData.append('content', updatedContent);

        fetch(`/api/board/${boardId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('게시글 수정에 실패했습니다.');
                }
                return response.json();
            })
            .then(() => {
                alert('게시글이 성공적으로 수정되었습니다.');
                boardTitle.disabled = true;
                boardContent.disabled = true;

                editButtonContainer.style.display = 'none';
                buttonContainer.style.display = 'block';
            })
            .catch(error => {
                console.error('게시글 수정 에러:', error.message);
                alert(`게시글 수정에 실패했습니다: ${error.message}`);
            });
    });

    // 삭제 버튼 클릭 이벤트
    deleteButton.addEventListener('click', function () {
        if (confirm('정말로 삭제하시겠습니까?')) {
            deleteBoard(boardId);
        }
    });

    // 게시글 삭제 요청
    function deleteBoard(boardId) {
        const accessToken = localStorage.getItem('accessToken');

        fetch(`/api/board/${boardId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
        })
            .then(response => {
                if (response.ok) {
                    alert('게시글이 삭제되었습니다.');
                    window.location.href = '/boardList.html'; // 삭제 후 목록 페이지로 이동
                } else {
                    return response.json().then(err => {
                        throw new Error(err.message || '게시글 삭제에 실패했습니다.');
                    });
                }
            })
            .catch(error => {
                console.error('게시글 삭제 오류:', error.message);
                alert(`삭제 실패: ${error.message}`);
            });
    }

    // 목록으로 돌아가기
    backToListButton.addEventListener('click', function () {
        window.location.href = '/boardList.html';
    });
});
