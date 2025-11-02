import { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from './ui/dialog';
import { Badge } from './ui/badge';
import { Plus, Edit, Trash2, Building2, RefreshCw, Star } from 'lucide-react';
import { companyService } from './api/services';
import { toast } from 'sonner@2.0.3';

interface Company {
  id?: number;
  name: string;
  description: string;
}

interface CompaniesProps {
  onNavigate: (page: string, companyId?: number) => void;
}

export function Companies({ onNavigate }: CompaniesProps) {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCompany, setEditingCompany] = useState<Company | null>(null);
  const [formData, setFormData] = useState<Company>({
    name: '',
    description: '',
  });

  useEffect(() => {
    loadCompanies();
  }, []);

  const loadCompanies = async () => {
    try {
      setLoading(true);
      const data = await companyService.getAllCompanies();
      setCompanies(data);
    } catch (error) {
      toast.error('Failed to load companies');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCompany) {
        await companyService.updateCompany(editingCompany.id!, formData);
        toast.success('Company updated successfully');
      } else {
        await companyService.createCompany(formData);
        toast.success('Company created successfully');
      }
      setDialogOpen(false);
      resetForm();
      loadCompanies();
    } catch (error) {
      toast.error(editingCompany ? 'Failed to update company' : 'Failed to create company');
      console.error(error);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this company? This will affect all related jobs.')) return;
    try {
      await companyService.deleteCompany(id);
      toast.success('Company deleted successfully');
      loadCompanies();
    } catch (error) {
      toast.error('Failed to delete company');
      console.error(error);
    }
  };

  const handleEdit = (company: Company) => {
    setEditingCompany(company);
    setFormData(company);
    setDialogOpen(true);
  };

  const resetForm = () => {
    setEditingCompany(null);
    setFormData({
      name: '',
      description: '',
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl mb-2">Companies Management</h1>
          <p className="text-muted-foreground">Manage all registered companies in the platform</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={loadCompanies} disabled={loading}>
            <RefreshCw className={`h-4 w-4 mr-2 ${loading ? 'animate-spin' : ''}`} />
            Refresh
          </Button>
          <Dialog open={dialogOpen} onOpenChange={(open) => {
            setDialogOpen(open);
            if (!open) resetForm();
          }}>
            <DialogTrigger asChild>
              <Button>
                <Plus className="h-4 w-4 mr-2" />
                Add Company
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>{editingCompany ? 'Edit Company' : 'Create New Company'}</DialogTitle>
                <DialogDescription>
                  {editingCompany ? 'Update company details below' : 'Fill in the details to register a new company'}
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <Label htmlFor="name">Company Name</Label>
                  <Input
                    id="name"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    placeholder="e.g., Tech Solutions Inc."
                    required
                  />
                </div>
                
                <div>
                  <Label htmlFor="description">Description</Label>
                  <textarea
                    id="description"
                    className="w-full min-h-32 px-3 py-2 border border-input bg-background rounded-md"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    placeholder="Brief description of the company, its mission, and what it does"
                    required
                  />
                </div>

                <div className="flex justify-end gap-2 pt-4">
                  <Button type="button" variant="outline" onClick={() => setDialogOpen(false)}>
                    Cancel
                  </Button>
                  <Button type="submit">
                    {editingCompany ? 'Update Company' : 'Create Company'}
                  </Button>
                </div>
              </form>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Companies Grid */}
      {loading ? (
        <div className="text-center py-8 text-muted-foreground">Loading companies...</div>
      ) : companies.length === 0 ? (
        <Card>
          <CardContent className="text-center py-12 text-muted-foreground">
            No companies found. Add your first company to get started!
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {companies.map((company) => (
            <Card key={company.id} className="hover:shadow-lg transition-all border-2 hover:border-primary">
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-12 w-12 bg-gradient-to-br from-primary to-blue-700 rounded-lg flex items-center justify-center">
                      <Building2 className="h-6 w-6 text-white" />
                    </div>
                    <div>
                      <CardTitle className="text-lg">{company.name}</CardTitle>
                      <Badge variant="secondary" className="mt-1">ID: {company.id}</Badge>
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <p className="text-sm text-muted-foreground line-clamp-3">{company.description}</p>
                
                <div className="flex items-center justify-between pt-4 border-t">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => onNavigate('reviews', company.id)}
                    className="flex items-center gap-1"
                  >
                    <Star className="h-3 w-3" />
                    View Reviews
                  </Button>
                  <div className="flex gap-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(company)}
                    >
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(company.id!)}
                    >
                      <Trash2 className="h-4 w-4 text-destructive" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Summary Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="border-2">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm">Total Companies</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl">{companies.length}</div>
            <p className="text-xs text-muted-foreground mt-1">Registered in platform</p>
          </CardContent>
        </Card>
        
        <Card className="border-2">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm">Active Companies</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl">{companies.length}</div>
            <p className="text-xs text-muted-foreground mt-1">Currently active</p>
          </CardContent>
        </Card>
        
        <Card className="border-2">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm">Platform Growth</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-green-600 dark:text-green-400">+{companies.length}</div>
            <p className="text-xs text-muted-foreground mt-1">Total registrations</p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
