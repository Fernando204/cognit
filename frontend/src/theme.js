const THEME_STORAGE_KEY = 'cognit-theme';

function getPreferredTheme() {
  const saved = localStorage.getItem(THEME_STORAGE_KEY);
  if (saved === 'light' || saved === 'dark') return saved;
  return 'dark';
}

export function applyTheme(theme) {
  const selectedTheme = theme === 'light' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', selectedTheme);
  localStorage.setItem(THEME_STORAGE_KEY, selectedTheme);

  const toggleButtons = document.querySelectorAll('[data-theme-toggle]');
  toggleButtons.forEach((button) => {
    const icon = button.querySelector('[data-theme-icon]');
    const label = button.querySelector('[data-theme-label]');

    if (icon) {
      icon.textContent = selectedTheme === 'dark' ? '☀' : '☾';
    }

    if (label) {
      label.textContent = selectedTheme === 'dark' ? 'Claro' : 'Escuro';
    }

    button.setAttribute('aria-label', selectedTheme === 'dark' ? 'Ativar modo claro' : 'Ativar modo escuro');
  });
}

export function initThemeToggle() {
  const initialTheme = getPreferredTheme();
  applyTheme(initialTheme);

  document.querySelectorAll('[data-theme-toggle]').forEach((button) => {
    button.addEventListener('click', () => {
      const currentTheme = document.documentElement.getAttribute('data-theme') === 'light' ? 'light' : 'dark';
      applyTheme(currentTheme === 'dark' ? 'light' : 'dark');
    });
  });
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    initThemeToggle();
  });
} else {
  initThemeToggle();
}
