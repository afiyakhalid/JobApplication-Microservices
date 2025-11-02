import { useState } from 'react';
import { Dashboard } from './components/Dashboard';
import { Jobs } from './components/Jobs';
import { Companies } from './components/Companies';
import { Reviews } from './components/Reviews';
import { ThemeProvider, useTheme } from './components/ThemeProvider';
import { Button } from './components/ui/button';
import { Toaster } from './components/ui/sonner';
import { Home, Briefcase, Building2, Star, Menu, X, Sun, Moon } from 'lucide-react';

type Page = 'dashboard' | 'jobs' | 'companies' | 'reviews';

function AppContent() {
  const [currentPage, setCurrentPage] = useState<Page>('dashboard');
  const [selectedCompanyId, setSelectedCompanyId] = useState<number | undefined>();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const { theme, toggleTheme } = useTheme();

  const handleNavigate = (page: string, companyId?: number) => {
    setCurrentPage(page as Page);
    if (companyId) {
      setSelectedCompanyId(companyId);
    }
    setMobileMenuOpen(false);
  };

  const navItems = [
    { id: 'dashboard', label: 'Home', icon: Home },
    { id: 'jobs', label: 'Jobs', icon: Briefcase },
    { id: 'companies', label: 'Companies', icon: Building2 },
    { id: 'reviews', label: 'Reviews', icon: Star },
  ];

  return (
    <div className="min-h-screen bg-background">
      {/* Top Navigation Bar */}
      <nav className="bg-card border-b border-border sticky top-0 z-50 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Logo and Title */}
            <div className="flex items-center gap-3">
              <div className="h-10 w-10 bg-gradient-to-br from-primary to-blue-700 rounded-lg flex items-center justify-center shadow-md">
                <Briefcase className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl">Job Management</h1>
                <p className="text-xs text-muted-foreground">Professional Platform</p>
              </div>
            </div>

            {/* Desktop Navigation */}
            <div className="hidden md:flex items-center gap-2">
              {navItems.map((item) => {
                const Icon = item.icon;
                return (
                  <Button
                    key={item.id}
                    variant={currentPage === item.id ? 'default' : 'ghost'}
                    onClick={() => handleNavigate(item.id)}
                    className="flex items-center gap-2"
                  >
                    <Icon className="h-4 w-4" />
                    {item.label}
                  </Button>
                );
              })}
              <div className="h-6 w-px bg-border mx-2" />
              <Button
                variant="outline"
                size="icon"
                onClick={toggleTheme}
                className="ml-2"
              >
                {theme === 'light' ? <Moon className="h-4 w-4" /> : <Sun className="h-4 w-4" />}
              </Button>
            </div>

            {/* Mobile Menu Button */}
            <div className="md:hidden flex items-center gap-2">
              <Button
                variant="outline"
                size="icon"
                onClick={toggleTheme}
              >
                {theme === 'light' ? <Moon className="h-4 w-4" /> : <Sun className="h-4 w-4" />}
              </Button>
              <Button
                variant="ghost"
                size="icon"
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              >
                {mobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
              </Button>
            </div>
          </div>

          {/* Mobile Navigation */}
          {mobileMenuOpen && (
            <div className="md:hidden pb-4 space-y-2">
              {navItems.map((item) => {
                const Icon = item.icon;
                return (
                  <Button
                    key={item.id}
                    variant={currentPage === item.id ? 'default' : 'ghost'}
                    onClick={() => handleNavigate(item.id)}
                    className="w-full flex items-center gap-2 justify-start"
                  >
                    <Icon className="h-4 w-4" />
                    {item.label}
                  </Button>
                );
              })}
            </div>
          )}
        </div>
      </nav>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {currentPage === 'dashboard' && <Dashboard onNavigate={handleNavigate} />}
        {currentPage === 'jobs' && <Jobs />}
        {currentPage === 'companies' && <Companies onNavigate={handleNavigate} />}
        {currentPage === 'reviews' && <Reviews selectedCompanyId={selectedCompanyId} />}
      </main>

      {/* Footer */}
      <footer className="bg-card border-t border-border mt-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div>
              <h3 className="mb-3 flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-primary" />
                Platform Features
              </h3>
              <ul className="text-sm text-muted-foreground space-y-2">
                <li>• Job Management System</li>
                <li>• Company Profiles</li>
                <li>• Review & Rating Platform</li>
                <li>• Real-time Updates</li>
              </ul>
            </div>
            <div>
              <h3 className="mb-3 flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-primary" />
                Key Benefits
              </h3>
              <ul className="text-sm text-muted-foreground space-y-2">
                <li>• Professional Interface</li>
                <li>• Secure Data Storage</li>
                <li>• Easy Navigation</li>
                <li>• Responsive Design</li>
              </ul>
            </div>
            <div>
              <h3 className="mb-3 flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-primary" />
                Platform Info
              </h3>
              <ul className="text-sm text-muted-foreground space-y-2">
                <li>• Client-side Application</li>
                <li>• Modern UI/UX Design</li>
                <li>• Dark Mode Support</li>
                <li>• Enterprise Ready</li>
              </ul>
            </div>
          </div>
          <div className="mt-8 pt-8 border-t border-border text-center text-sm text-muted-foreground">
            <p>© 2025 Job Management Platform. All rights reserved.</p>
            <p className="mt-1">Professional solution for managing jobs, companies, and reviews.</p>
          </div>
        </div>
      </footer>

      {/* Toast Notifications */}
      <Toaster position="top-right" />
    </div>
  );
}

export default function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}
