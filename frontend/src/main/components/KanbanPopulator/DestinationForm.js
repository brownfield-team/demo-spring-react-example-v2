import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import ReactJson from "react-json-view";

export default function DestinationForm(props) {
  const { onSubmit, destination } = props;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Form.Group className="mb-3">
        <Form.Label htmlFor="destOrg">Destination Organization</Form.Label>
        <Form.Control
          id="destOrg"
          type="text"
          isInvalid={Boolean(errors.org)}
          {...register("org", { required: "Destination Organization is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.org?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="destRepo">Destination Repository</Form.Label>
        <Form.Control
          id="destRepo"
          type="text"
          isInvalid={Boolean(errors.org)}
          {...register("repo", { required: "Destination Repository is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.repo?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="destProj">Destination Project Name</Form.Label>
        <Form.Control
          id="destProj"
          type="text"
          isInvalid={Boolean(errors.proj)}
          {...register("proj", { required: "Destination Project Name is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.proj?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" data-testid="DestinationForm-Submit-Button">Submit Destination</Button>
      <ReactJson src={destination} />
    </Form>
  );
}
