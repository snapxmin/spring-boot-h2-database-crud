// API Base URL
const API_URL = '/api/tutorials';

// DOM Elements
const tutorialForm = document.getElementById('tutorial-form');
const tutorialIdInput = document.getElementById('tutorial-id');
const titleInput = document.getElementById('title');
const descriptionInput = document.getElementById('description');
const publishedInput = document.getElementById('published');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const formTitle = document.getElementById('form-title');
const searchInput = document.getElementById('search-input');
const searchBtn = document.getElementById('search-btn');
const showAllBtn = document.getElementById('show-all-btn');
const showPublishedBtn = document.getElementById('show-published-btn');
const deleteAllBtn = document.getElementById('delete-all-btn');
const tutorialsList = document.getElementById('tutorials-list');
const emptyMessage = document.getElementById('empty-message');
const modal = document.getElementById('modal');
const modalTitle = document.getElementById('modal-title');
const modalMessage = document.getElementById('modal-message');
const modalConfirm = document.getElementById('modal-confirm');
const modalCancel = document.getElementById('modal-cancel');
const toast = document.getElementById('toast');

// State
let editingTutorialId = null;
let deleteCallback = null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadAllTutorials();
    setupEventListeners();
});

// Event Listeners
function setupEventListeners() {
    tutorialForm.addEventListener('submit', handleSubmit);
    cancelBtn.addEventListener('click', resetForm);
    searchBtn.addEventListener('click', handleSearch);
    showAllBtn.addEventListener('click', loadAllTutorials);
    showPublishedBtn.addEventListener('click', loadPublishedTutorials);
    deleteAllBtn.addEventListener('click', confirmDeleteAll);
    modalCancel.addEventListener('click', closeModal);
    
    // Search on Enter key
    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
}

// API Functions
async function loadAllTutorials() {
    try {
        const response = await fetch(API_URL);
        if (response.status === 204) {
            displayTutorials([]);
        } else if (response.ok) {
            const tutorials = await response.json();
            displayTutorials(tutorials);
        } else {
            showToast('Failed to load tutorials', 'error');
        }
    } catch (error) {
        console.error('Error loading tutorials:', error);
        showToast('Error loading tutorials', 'error');
    }
}

async function loadPublishedTutorials() {
    try {
        const response = await fetch(`${API_URL}/published`);
        if (response.status === 204) {
            displayTutorials([]);
        } else if (response.ok) {
            const tutorials = await response.json();
            displayTutorials(tutorials);
        } else {
            showToast('Failed to load published tutorials', 'error');
        }
    } catch (error) {
        console.error('Error loading published tutorials:', error);
        showToast('Error loading published tutorials', 'error');
    }
}

async function searchTutorials(title) {
    try {
        const response = await fetch(`${API_URL}?title=${encodeURIComponent(title)}`);
        if (response.status === 204) {
            displayTutorials([]);
        } else if (response.ok) {
            const tutorials = await response.json();
            displayTutorials(tutorials);
        } else {
            showToast('Failed to search tutorials', 'error');
        }
    } catch (error) {
        console.error('Error searching tutorials:', error);
        showToast('Error searching tutorials', 'error');
    }
}

async function createTutorial(tutorial) {
    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(tutorial),
        });
        
        if (response.ok) {
            const newTutorial = await response.json();
            showToast('Tutorial created successfully!', 'success');
            resetForm();
            loadAllTutorials();
            return newTutorial;
        } else {
            showToast('Failed to create tutorial', 'error');
        }
    } catch (error) {
        console.error('Error creating tutorial:', error);
        showToast('Error creating tutorial', 'error');
    }
}

async function updateTutorial(id, tutorial) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(tutorial),
        });
        
        if (response.ok) {
            const updatedTutorial = await response.json();
            showToast('Tutorial updated successfully!', 'success');
            resetForm();
            loadAllTutorials();
            return updatedTutorial;
        } else {
            showToast('Failed to update tutorial', 'error');
        }
    } catch (error) {
        console.error('Error updating tutorial:', error);
        showToast('Error updating tutorial', 'error');
    }
}

async function deleteTutorial(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });
        
        if (response.ok || response.status === 204) {
            showToast('Tutorial deleted successfully!', 'success');
            loadAllTutorials();
        } else {
            showToast('Failed to delete tutorial', 'error');
        }
    } catch (error) {
        console.error('Error deleting tutorial:', error);
        showToast('Error deleting tutorial', 'error');
    }
}

async function deleteAllTutorials() {
    try {
        const response = await fetch(API_URL, {
            method: 'DELETE',
        });
        
        if (response.ok || response.status === 204) {
            showToast('All tutorials deleted successfully!', 'success');
            loadAllTutorials();
        } else {
            showToast('Failed to delete all tutorials', 'error');
        }
    } catch (error) {
        console.error('Error deleting all tutorials:', error);
        showToast('Error deleting all tutorials', 'error');
    }
}

// Event Handlers
function handleSubmit(e) {
    e.preventDefault();
    
    const tutorial = {
        title: titleInput.value.trim(),
        description: descriptionInput.value.trim(),
        published: publishedInput.checked,
    };
    
    if (editingTutorialId) {
        updateTutorial(editingTutorialId, tutorial);
    } else {
        createTutorial(tutorial);
    }
}

function handleSearch() {
    const searchTerm = searchInput.value.trim();
    if (searchTerm) {
        searchTutorials(searchTerm);
    } else {
        loadAllTutorials();
    }
}

function handleEdit(tutorial) {
    editingTutorialId = tutorial.id;
    titleInput.value = tutorial.title;
    descriptionInput.value = tutorial.description;
    publishedInput.checked = tutorial.published;
    
    formTitle.textContent = '✏️ Edit Tutorial';
    submitBtn.querySelector('span').textContent = 'Update Tutorial';
    cancelBtn.style.display = 'inline-flex';
    
    // Scroll to form
    document.querySelector('.form-card').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function handleDelete(id) {
    deleteCallback = () => deleteTutorial(id);
    showModal('Delete Tutorial', 'Are you sure you want to delete this tutorial?');
}

function confirmDeleteAll() {
    deleteCallback = deleteAllTutorials;
    showModal('Delete All Tutorials', 'Are you sure you want to delete all tutorials? This action cannot be undone.');
}

function resetForm() {
    tutorialForm.reset();
    editingTutorialId = null;
    formTitle.textContent = '➕ Add New Tutorial';
    submitBtn.querySelector('span').textContent = 'Add Tutorial';
    cancelBtn.style.display = 'none';
}

// Display Functions
function displayTutorials(tutorials) {
    tutorialsList.innerHTML = '';
    
    if (tutorials.length === 0) {
        emptyMessage.style.display = 'block';
        return;
    }
    
    emptyMessage.style.display = 'none';
    
    tutorials.forEach(tutorial => {
        const tutorialElement = createTutorialElement(tutorial);
        tutorialsList.appendChild(tutorialElement);
    });
}

function createTutorialElement(tutorial) {
    const div = document.createElement('div');
    div.className = `tutorial-item ${tutorial.published ? 'published' : ''}`;
    
    div.innerHTML = `
        <div class="tutorial-header">
            <div class="tutorial-title">${escapeHtml(tutorial.title)}</div>
            <span class="tutorial-badge ${tutorial.published ? 'published' : 'draft'}">
                ${tutorial.published ? '✓ Published' : '📝 Draft'}
            </span>
        </div>
        <div class="tutorial-description">${escapeHtml(tutorial.description)}</div>
        <div class="tutorial-actions">
            <button class="btn btn-primary btn-edit" data-id="${tutorial.id}">
                <span>Edit</span>
            </button>
            <button class="btn btn-danger btn-delete" data-id="${tutorial.id}">
                <span>Delete</span>
            </button>
        </div>
    `;
    
    // Add event listeners
    const editBtn = div.querySelector('.btn-edit');
    const deleteBtn = div.querySelector('.btn-delete');
    
    editBtn.addEventListener('click', () => handleEdit(tutorial));
    deleteBtn.addEventListener('click', () => handleDelete(tutorial.id));
    
    return div;
}

// Modal Functions
function showModal(title, message) {
    modalTitle.textContent = title;
    modalMessage.textContent = message;
    modal.style.display = 'block';
    
    modalConfirm.onclick = () => {
        if (deleteCallback) {
            deleteCallback();
            deleteCallback = null;
        }
        closeModal();
    };
}

function closeModal() {
    modal.style.display = 'none';
    deleteCallback = null;
}

// Close modal when clicking outside
window.addEventListener('click', (e) => {
    if (e.target === modal) {
        closeModal();
    }
});

// Toast Notification
function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Utility Functions
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}
