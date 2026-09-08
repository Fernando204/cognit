const mainContent = document.querySelector('.main-content');

// título e descrição usados quando a página ainda não tem conteúdo próprio
const PAGE_META = {
  'principal/visao-geral': { title: 'Visão Geral' },
  'principal/metas': { title: 'Metas', desc: 'Acompanhamento das metas do negócio.' },
  'principal/colaboradores': { title: 'Colaboradores', desc: 'Gestão da equipe e permissões.' },
  'principal/historico': { title: 'Histórico', desc: 'Linha do tempo de eventos do sistema.' },

  'estoque/produtos': { title: 'Produtos', desc: 'Catálogo e controle de produtos.' },
  'estoque/encomendas': { title: 'Encomendas', desc: 'Encomendas em aberto e concluídas.' },
  'estoque/fornecedores': { title: 'Fornecedores', desc: 'Cadastro e relacionamento com fornecedores.' },
  'estoque/historico-movimentacoes': { title: 'Histórico de Movimentações', desc: 'Entradas e saídas do estoque.' },

  'financeiro/dashboard': { title: 'Dashboard Financeiro', desc: 'Panorama financeiro do negócio.' },
  'financeiro/fluxo': { title: 'Fluxo de Caixa', desc: 'Entradas e saídas financeiras.' },
  'financeiro/lancamentos-recorrentes': { title: 'Lançamentos Recorrentes', desc: 'Contas e receitas que se repetem.' },
  'financeiro/contas': { title: 'Contas', desc: 'Contas a pagar e a receber.' },

  'vendas/dashboard': { title: 'Dashboard de Vendas', desc: 'Panorama comercial do negócio.' },
  'vendas/clientes': { title: 'Clientes', desc: 'Base de clientes cadastrados.' },
  'vendas/nova-venda': { title: 'Nova Venda', desc: 'Registrar uma nova venda.' },
  'vendas/lista-vendas': { title: 'Lista de Vendas', desc: 'Histórico de vendas realizadas.' },
  'vendas/pedidos': { title: 'Pedidos', desc: 'Pedidos em andamento e finalizados.' },
};

let currentController = null;

export async function navigateTo(page) {
  if (!mainContent) return;

  // cancela um carregamento anterior, se houver
  if (currentController) currentController.abort();
  currentController = new AbortController();

  try {
    const response = await fetch(`pages/${page}.html`, { signal: currentController.signal });

    if (!response.ok) throw new Error(`Página não encontrada: ${page}`);

    const html = await response.text();
    mainContent.innerHTML = html;

    await loadPageScript(page);
  } catch (error) {
    if (error.name === 'AbortError') return;
    console.error(error);
    mainContent.innerHTML = renderFallback(page);
  }
}

async function loadPageScript(page) {
  try {
    const module = await import(`./pages/${page}.js`);
    if (module && typeof module.init === 'function') {
      module.init(mainContent);
    }
  } catch (error) {
    // página ainda sem lógica própria — tudo bem, é apenas o placeholder
  }
}

function renderFallback(page) {
  const meta = PAGE_META[page] || { title: 'Página', desc: 'Conteúdo em construção.' };

  return `
    <section class="page">
      <div class="page-header">
        <h1>${meta.title}</h1>
        <p>${meta.desc || ''}</p>
      </div>
      <div class="placeholder-page">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 9v4"></path>
          <path d="M12 17h.01"></path>
          <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0Z"></path>
        </svg>
        <h2>Página em construção</h2>
        <span>Este módulo ainda está sendo desenvolvido.</span>
      </div>
    </section>
  `;
}
