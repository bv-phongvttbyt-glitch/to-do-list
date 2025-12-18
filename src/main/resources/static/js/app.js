const apiBase = '/api/tasks';

const taskListEl = document.getElementById('task-list');
const form = document.getElementById('task-form');
const refreshBtn = document.getElementById('refresh-btn');

async function fetchJson(url, options = {}) {
    const response = await fetch(url, {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    });
    if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        const message = body.error || response.statusText;
        throw new Error(message);
    }
    return response.json();
}

function renderTasks(tasks) {
    if (tasks.length === 0) {
        taskListEl.innerHTML = '<li class="empty-state">Chưa có công việc nào.</li>';
        return;
    }

    taskListEl.innerHTML = '';
    tasks.forEach(task => {
        const li = document.createElement('li');
        li.className = 'task-item';

        const left = document.createElement('div');
        const title = document.createElement('p');
        title.className = 'task-title';
        title.textContent = task.title;
        left.appendChild(title);

        if (task.description) {
            const desc = document.createElement('p');
            desc.textContent = task.description;
            left.appendChild(desc);
        }

        const meta = document.createElement('div');
        meta.className = 'task-meta';
        const updated = new Date(task.updatedAt).toLocaleString();
        meta.textContent = `Cập nhật: ${updated}`;
        left.appendChild(meta);

        const status = document.createElement('span');
        status.className = `status-chip ${task.completed ? 'done' : 'open'}`;
        status.textContent = task.completed ? 'Hoàn thành' : 'Đang mở';
        left.appendChild(status);

        const actions = document.createElement('div');
        actions.className = 'task-actions';

        const toggleBtn = document.createElement('button');
        toggleBtn.className = task.completed ? 'secondary' : 'success';
        toggleBtn.textContent = task.completed ? 'Đánh dấu chưa xong' : 'Đánh dấu xong';
        toggleBtn.onclick = async () => {
            try {
                await fetchJson(`${apiBase}/${task.id}/toggle`, { method: 'POST' });
                await loadTasks();
            } catch (err) {
                alert(err.message);
            }
        };
        actions.appendChild(toggleBtn);

        const editBtn = document.createElement('button');
        editBtn.className = 'secondary';
        editBtn.textContent = 'Sửa';
        editBtn.onclick = () => openEdit(task);
        actions.appendChild(editBtn);

        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'danger';
        deleteBtn.textContent = 'Xoá';
        deleteBtn.onclick = async () => {
            if (!confirm('Bạn có chắc muốn xoá công việc này?')) return;
            try {
                await fetch(`${apiBase}/${task.id}`, { method: 'DELETE' });
                await loadTasks();
            } catch (err) {
                alert(err.message);
            }
        };
        actions.appendChild(deleteBtn);

        li.appendChild(left);
        li.appendChild(actions);
        taskListEl.appendChild(li);
    });
}

function openEdit(task) {
    const title = prompt('Cập nhật tiêu đề', task.title);
    if (title === null) return;
    const description = prompt('Cập nhật mô tả', task.description || '');
    const completed = confirm('Đánh dấu hoàn thành? OK = có, Cancel = không');
    updateTask(task.id, { title, description, completed });
}

async function updateTask(id, payload) {
    try {
        await fetchJson(`${apiBase}/${id}`, {
            method: 'PUT',
            body: JSON.stringify(payload),
        });
        await loadTasks();
    } catch (err) {
        alert(err.message);
    }
}

async function loadTasks() {
    try {
        const tasks = await fetchJson(apiBase);
        renderTasks(tasks);
    } catch (err) {
        alert(err.message);
    }
}

form.addEventListener('submit', async event => {
    event.preventDefault();
    const formData = new FormData(form);
    const payload = Object.fromEntries(formData.entries());
    payload.completed = false;

    try {
        await fetchJson(apiBase, {
            method: 'POST',
            body: JSON.stringify(payload),
        });
        form.reset();
        await loadTasks();
    } catch (err) {
        alert(err.message);
    }
});

refreshBtn.addEventListener('click', loadTasks);

loadTasks();
