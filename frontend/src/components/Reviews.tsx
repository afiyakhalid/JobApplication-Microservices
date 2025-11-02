import { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from './ui/dialog';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Badge } from './ui/badge';
import { Plus, Edit, Trash2, Star, RefreshCw, StarIcon } from 'lucide-react';
import { reviewService, companyService } from './api/services';
import { toast } from 'sonner@2.0.3';

interface Review {
  id?: number;
  title: string;
  description: string;
  rating?: number;
  companyId: number;
}

interface Company {
  id: number;
  name: string;
  description: string;
}

interface ReviewsProps {
  selectedCompanyId?: number;
}

export function Reviews({ selectedCompanyId }: ReviewsProps) {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [companies, setCompanies] = useState<Company[]>([]);
  const [selectedCompany, setSelectedCompany] = useState<number | null>(selectedCompanyId || null);
  const [averageRating, setAverageRating] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingReview, setEditingReview] = useState<Review | null>(null);
  const [formData, setFormData] = useState<Review>({
    title: '',
    description: '',
    rating: 5,
    companyId: 0,
  });

  useEffect(() => {
    loadCompanies();
  }, []);

  useEffect(() => {
    if (selectedCompany) {
      loadReviews(selectedCompany);
      loadAverageRating(selectedCompany);
    }
  }, [selectedCompany]);

  const loadCompanies = async () => {
    try {
      const data = await companyService.getAllCompanies();
      setCompanies(data);
      if (data.length > 0 && !selectedCompany) {
        setSelectedCompany(data[0].id);
      }
    } catch (error) {
      console.error('Failed to load companies:', error);
    }
  };

  const loadReviews = async (companyId: number) => {
    try {
      setLoading(true);
      const data = await reviewService.getReviewsByCompany(companyId);
      setReviews(data);
    } catch (error) {
      toast.error('Failed to load reviews');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const loadAverageRating = async (companyId: number) => {
    try {
      const data = await reviewService.getAverageRating(companyId);
      setAverageRating(data.averageRating || null);
    } catch (error) {
      console.error('Failed to load average rating:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedCompany) {
      toast.error('Please select a company');
      return;
    }

    try {
      if (editingReview) {
        await reviewService.updateReview(editingReview.id!, formData);
        toast.success('Review updated successfully');
      } else {
        await reviewService.createReview(selectedCompany, formData);
        toast.success('Review created successfully');
      }
      setDialogOpen(false);
      resetForm();
      loadReviews(selectedCompany);
      loadAverageRating(selectedCompany);
    } catch (error) {
      toast.error(editingReview ? 'Failed to update review' : 'Failed to create review');
      console.error(error);
    }
  };

  const handleDelete = async (reviewId: number) => {
    if (!confirm('Are you sure you want to delete this review?')) return;
    try {
      await reviewService.deleteReview(reviewId);
      toast.success('Review deleted successfully');
      if (selectedCompany) {
        loadReviews(selectedCompany);
        loadAverageRating(selectedCompany);
      }
    } catch (error) {
      toast.error('Failed to delete review');
      console.error(error);
    }
  };

  const handleEdit = (review: Review) => {
    setEditingReview(review);
    setFormData(review);
    setDialogOpen(true);
  };

  const resetForm = () => {
    setEditingReview(null);
    setFormData({
      title: '',
      description: '',
      rating: 5,
      companyId: 0,
    });
  };

  const getCompanyName = (companyId: number) => {
    const company = companies.find(c => c.id === companyId);
    return company?.name || 'Unknown Company';
  };

  const renderStars = (rating: number = 0) => {
    return (
      <div className="flex gap-1">
        {[1, 2, 3, 4, 5].map((star) => (
          <Star
            key={star}
            className={`h-4 w-4 ${star <= rating ? 'fill-yellow-500 text-yellow-500 dark:fill-yellow-400 dark:text-yellow-400' : 'text-gray-300 dark:text-gray-600'}`}
          />
        ))}
      </div>
    );
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl mb-2">Reviews Management</h1>
          <p className="text-muted-foreground">Manage company reviews and ratings</p>
        </div>
        <div className="flex gap-2">
          <Button 
            variant="outline" 
            onClick={() => selectedCompany && loadReviews(selectedCompany)} 
            disabled={loading || !selectedCompany}
          >
            <RefreshCw className={`h-4 w-4 mr-2 ${loading ? 'animate-spin' : ''}`} />
            Refresh
          </Button>
          <Dialog open={dialogOpen} onOpenChange={(open) => {
            setDialogOpen(open);
            if (!open) resetForm();
          }}>
            <DialogTrigger asChild>
              <Button disabled={!selectedCompany}>
                <Plus className="h-4 w-4 mr-2" />
                Add Review
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>{editingReview ? 'Edit Review' : 'Create New Review'}</DialogTitle>
                <DialogDescription>
                  {editingReview ? 'Update review details below' : 'Share your experience with this company'}
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <Label htmlFor="title">Review Title</Label>
                  <Input
                    id="title"
                    value={formData.title}
                    onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    placeholder="e.g., Great place to work!"
                    required
                  />
                </div>
                
                <div>
                  <Label htmlFor="description">Review Description</Label>
                  <textarea
                    id="description"
                    className="w-full min-h-32 px-3 py-2 border border-input bg-background rounded-md"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    placeholder="Share your detailed experience"
                    required
                  />
                </div>

                <div>
                  <Label htmlFor="rating">Rating</Label>
                  <Select
                    value={formData.rating?.toString() || '5'}
                    onValueChange={(value) => setFormData({ ...formData, rating: Number(value) })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select rating" />
                    </SelectTrigger>
                    <SelectContent>
                      {[5, 4, 3, 2, 1].map((rating) => (
                        <SelectItem key={rating} value={rating.toString()}>
                          {rating} Star{rating !== 1 ? 's' : ''}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                <div className="flex justify-end gap-2 pt-4">
                  <Button type="button" variant="outline" onClick={() => setDialogOpen(false)}>
                    Cancel
                  </Button>
                  <Button type="submit">
                    {editingReview ? 'Update Review' : 'Create Review'}
                  </Button>
                </div>
              </form>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Company Selector */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>Select Company</CardTitle>
          <CardDescription>Choose a company to view and manage its reviews</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4 items-end">
            <div className="flex-1">
              <Label htmlFor="companySelect">Company</Label>
              <Select
                value={selectedCompany?.toString() || ''}
                onValueChange={(value) => setSelectedCompany(Number(value))}
              >
                <SelectTrigger id="companySelect">
                  <SelectValue placeholder="Select a company" />
                </SelectTrigger>
                <SelectContent>
                  {companies.map((company) => (
                    <SelectItem key={company.id} value={company.id.toString()}>
                      {company.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            {selectedCompany && averageRating !== null && (
              <Card className="px-4 py-3 border-2">
                <div className="text-sm text-muted-foreground">Average Rating</div>
                <div className="flex items-center gap-2 mt-1">
                  <StarIcon className="h-5 w-5 fill-yellow-500 text-yellow-500 dark:fill-yellow-400 dark:text-yellow-400" />
                  <span className="text-xl">{averageRating.toFixed(1)}</span>
                </div>
              </Card>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Reviews List */}
      {selectedCompany ? (
        loading ? (
          <div className="text-center py-8 text-muted-foreground">Loading reviews...</div>
        ) : reviews.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12 text-muted-foreground">
              No reviews found for {getCompanyName(selectedCompany)}. Be the first to add a review!
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {reviews.map((review) => (
              <Card key={review.id} className="hover:shadow-lg transition-all border-2 hover:border-primary">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg mb-2">{review.title}</CardTitle>
                      {renderStars(review.rating)}
                    </div>
                    <Badge variant="outline">ID: {review.id}</Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <p className="text-sm text-muted-foreground">{review.description}</p>
                  
                  <div className="flex justify-end gap-2 pt-4 border-t">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(review)}
                    >
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(review.id!)}
                    >
                      <Trash2 className="h-4 w-4 text-destructive" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )
      ) : (
        <Card>
          <CardContent className="text-center py-12 text-muted-foreground">
            Please select a company to view reviews
          </CardContent>
        </Card>
      )}
    </div>
  );
}
